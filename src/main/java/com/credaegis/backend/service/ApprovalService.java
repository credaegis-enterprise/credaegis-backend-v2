package com.credaegis.backend.service;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.ApprovalsInfoDTO;
import com.credaegis.backend.entity.Approval;
import com.credaegis.backend.entity.Event;
import com.credaegis.backend.entity.Status;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.ApprovalRepository;
import com.credaegis.backend.repository.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.ulid.UlidCreator;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final EventRepository eventRepository;
    private final MinioClient minioClient;

    public void uploadApprovals(String eventId, String userId, String organizationId,
                                List<MultipartFile> approvalsCertificates, String approvalsInfo) throws JsonProcessingException {

        Event event = eventRepository.findById(eventId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!event.getCluster().getOrganization().getId().equals(organizationId))
            throw ExceptionFactory.insufficientPermission();

        String clusterId = event.getCluster().getId();



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

        String approvalPath = event.getCluster().getName()+"-"+clusterId +
                "/" + event.getName()+"-"+eventId;
        for (ApprovalsInfoDTO info : approvalsInfoDTOS) {
//            if (!approvalsCertificatesMap.containsKey(info.getFileName())) {
//                //tobe done
//            }
            try {
                String approvalCertificateId = UlidCreator.getUlid().toString();
                MultipartFile uploadCertificate = approvalsCertificatesMap.get(info.getFileName());
                minioClient.putObject(PutObjectArgs.builder().bucket("approvals")
                        .object(approvalPath + "/" + info.getFileName()+"-"+approvalCertificateId)
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
