package com.credaegis.backend.service;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.ApprovalsInfoDTO;
import com.credaegis.backend.entity.Approval;
import com.credaegis.backend.entity.Event;
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

    public void uploadApprovals(String eventId, String clusterId, String userId, String organizationId,
                                List<MultipartFile> approvalsCertificates, String approvalsInfo) throws JsonProcessingException {

        Event event = eventRepository.findById(eventId).orElseThrow(ExceptionFactory::resourceNotFound);
        if(!event.getCluster().getOrganization().getId().equals(organizationId))
            throw ExceptionFactory.insufficientPermission();
        if(!event.getCluster().getId().equals(clusterId))
            throw ExceptionFactory.customValidationError("Event is not in the cluster");

        String approvalPath=clusterId+"/"+eventId;
        ObjectMapper objectMapper = new ObjectMapper();
        List<ApprovalsInfoDTO> approvalsInfoDTOS = objectMapper
                .readValue(approvalsInfo, new TypeReference<List<ApprovalsInfoDTO>>() {
        });

//        for(ApprovalsInfoDTO test: approvalsInfoDTOS){
//            System.out.println(test.getRecipientEmail());
//            System.out.println(test.getFileName());
//        }
        Map<String,MultipartFile> approvalsCertificatesMap = new HashMap<>();

        //checks for duplicate filenames
        for(MultipartFile certificate: approvalsCertificates){
            if(approvalsCertificatesMap.containsKey(certificate.getName()))
                throw ExceptionFactory.customValidationError("Duplicate filename "+certificate.getOriginalFilename()+" found");
            else
                approvalsCertificatesMap.put(certificate.getOriginalFilename(),certificate);

        }

        for(ApprovalsInfoDTO info:approvalsInfoDTOS){
            if(!approvalsCertificatesMap.containsKey(info.getFileName())){
                //tobe done
            }
            try {
                MultipartFile uploadCertificate = approvalsCertificatesMap.get(info.getFileName());
                minioClient.putObject(PutObjectArgs.builder().bucket("approvals")
                        .object(approvalPath + "/" + info.getFileName())
                        .stream(uploadCertificate.getInputStream(), uploadCertificate.getSize(), -1)
                        .build());


                Approval approval = new Approval();
                approval.setId(UlidCreator.getUlid().toString());
                approval.setRecipientEmail(info.getRecipientEmail());
                approval.setRecipientName(info.getRecipientName());
                approval.setFileName(info.getFileName());
                approval.setEvent(event);
                approval.setComments(info.getComments());
                approval.setExpiryDate(info.getExpiryDate());
            }
            catch (Exception e){
                log.error(e.getMessage());
                log.error("error uploading file {}",info.getFileName());
                ExceptionFactory.customValidationError("Error uploading file "+info.getFileName());
            }



        }

    }
}
