package com.credaegis.backend.controller;


import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.ApprovalsInfoDTO;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.service.ApprovalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = Constants.ROUTEV1 + "/approval-control")
@AllArgsConstructor
public class ApprovalController {


    private final ApprovalService approvalService;

    @PostMapping(path = "/upload/{eventId}")
    public ResponseEntity<CustomApiResponse<Void>> uploadCertificates(
                                                                      @PathVariable String eventId,
                                                                      @RequestParam("approvals") List<MultipartFile> approvalCertificates,
                                                                      @RequestParam("info") String approvalsInfo,
                                                                      @AuthenticationPrincipal CustomUser customUser) throws JsonProcessingException{

        approvalService.uploadApprovals(eventId,customUser.getId(),customUser.getOrganizationId(),
                approvalCertificates,approvalsInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CustomApiResponse<>(null,"certificates for approvals uploaded successfully",true)
        );

    }
}
