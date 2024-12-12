package com.credaegis.backend.service;


import com.credaegis.backend.dto.ApprovalsInfoDTO;
import com.credaegis.backend.entity.Approval;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.entity.Event;
import com.credaegis.backend.entity.Status;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.ApprovalRepository;
import com.credaegis.backend.repository.CertificateRepository;
import com.credaegis.backend.repository.EventRepository;
import com.credaegis.backend.utility.CheckSumUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.ulid.UlidCreator;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final CertificateRepository certificateRepository;
    private final EventRepository eventRepository;
    private final MinioClient minioClient;
    private final CheckSumUtility checkSumUtility;


    public void approveCertificates(String userId, String userOrganizationId, List<String> approvalIdList) {
        for (String approvalId : approvalIdList) {
            try {
                Approval approval = approvalRepository.findById(approvalId).orElseThrow(ExceptionFactory::resourceNotFound);
                if (!approval.getEvent().getCluster().getOrganization().getId().equals(userOrganizationId)) {
                    throw ExceptionFactory.insufficientPermission();
                }
                String approvalPath = approval.getEvent().getCluster().getName() + "-" + approval.getEvent().getCluster().getId() + "/"
                        + approval.getEvent().getName() + "-" + approval.getEvent().getId() + "/" + approval.getApprovalCertificateName()
                        + "-" + approval.getApprovalCertificateId();

                System.out.println(approvalPath);

                InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                        .bucket("approvals")
                        .object(approvalPath)
                        .build());

                String hashedValue = checkSumUtility.hashCertificate(stream.readAllBytes());

                //creating new certificate approving them blockchain integration here(blockchain queue)
                Certificate certificate = new Certificate();
                certificate.setId(UlidCreator.getUlid().toString());
                certificate.setCertificateName(approval.getApprovalCertificateName());
                certificate.setCertificateHash(hashedValue);
                certificate.setComments(approval.getComments());
                certificate.setRecipientName(approval.getRecipientName());
                certificate.setRecipientEmail(approval.getRecipientEmail());
                certificate.setIssuedDate(new Date(System.currentTimeMillis()));
                certificate.setEvent(approval.getEvent());

                //right now storing everything in off-chain database
                certificateRepository.save(certificate);


            } catch (Exception e) {

                //error queue here
                log.error(e.getMessage());
            }


        }
    }


    public void uploadApprovals(String eventId, String userId, String userOrganizationId,
                                List<MultipartFile> approvalsCertificates, String approvalsInfo) throws JsonProcessingException {

        Event event = eventRepository.findById(eventId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!event.getCluster().getOrganization().getId().equals(userOrganizationId))
            throw ExceptionFactory.insufficientPermission();


        //To serialize the info which comes along with the file
        ObjectMapper objectMapper = new ObjectMapper();
        List<ApprovalsInfoDTO> approvalsInfoDTOS = objectMapper
                .readValue(approvalsInfo, new TypeReference<List<ApprovalsInfoDTO>>() {
                });

        Map<String, MultipartFile> approvalsCertificatesMap = new HashMap<>();

        //checks for duplicate filenames
        for (MultipartFile certificate : approvalsCertificates) {
            if (approvalsCertificatesMap.containsKey(certificate.getName()))
                throw ExceptionFactory.customValidationError("Duplicate filename " + certificate.getOriginalFilename() + " found");
            else
                approvalsCertificatesMap.put(certificate.getOriginalFilename(), certificate);

        }

        //path to store in minio
        String clusterId = event.getCluster().getId();
        String approvalPath = event.getCluster().getName() + "-" + clusterId +
                "/" + event.getName() + "-" + eventId;
        for (ApprovalsInfoDTO info : approvalsInfoDTOS) {
//            if (!approvalsCertificatesMap.containsKey(info.getFileName())) {
//                to be done
//            }
            try {
                String approvalCertificateId = UlidCreator.getUlid().toString();
                MultipartFile uploadCertificate = approvalsCertificatesMap.get(info.getFileName());
                minioClient.putObject(PutObjectArgs.builder().bucket("approvals")
                        .object(approvalPath + "/" + info.getFileName() + "-" + approvalCertificateId)
                        .stream(uploadCertificate.getInputStream(), uploadCertificate.getSize(), -1)
                        .build());


                Approval approval = new Approval();
                approval.setId(UlidCreator.getUlid().toString());
                approval.setApprovalCertificateId(approvalCertificateId);
                approval.setApprovalCertificateName(info.getFileName());
                approval.setRecipientEmail(info.getRecipientEmail());
                approval.setRecipientName(info.getRecipientName());
                approval.setEvent(event);
                approval.setStatus(Status.pending);
                approval.setComments(info.getComments());
                approval.setExpiryDate(info.getExpiryDate());
                approvalRepository.save(approval);
            } catch (Exception e) {

                //error queue here
                log.error(e.getMessage());
                log.error("error uploading file {}", info.getFileName());
            }


        }

    }
}
