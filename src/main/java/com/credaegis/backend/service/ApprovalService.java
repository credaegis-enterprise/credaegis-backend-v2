package com.credaegis.backend.service;


import com.credaegis.backend.dto.ApprovalsInfoDTO;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.ApprovalRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ApprovalService {

    pirvate final ApprovalRepository approvalRepository;

    public void uploadApprovals(String eventId, String clusterId, String userId, String organizationId,
                                List<MultipartFile> approvalsCertificates, String approvalsInfo) throws JsonProcessingException {


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
                approvalsCertificatesMap.put(certificate.getName(),certificate);

        }

        for(ApprovalsInfoDTO info:approvalsInfoDTOS){

        }

    }
}
