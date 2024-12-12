package com.credaegis.backend.controller;


import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.request.ApproveCertificatesRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.service.ApprovalService;
import com.credaegis.backend.utility.CheckSumUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


@RestController
@RequestMapping(value = Constants.ROUTEV1 + "/approval-control")
@AllArgsConstructor
public class ApprovalController {


    private final ApprovalService approvalService;
    private final CheckSumUtility checkSumUtility;

    @PostMapping(path = "/upload/{eventId}")
    public ResponseEntity<CustomApiResponse<Void>> uploadCertificates(
            @PathVariable String eventId,
            @RequestParam("approvals") List<MultipartFile> approvalCertificates,
            @RequestParam("info") String approvalsInfo,
            @AuthenticationPrincipal CustomUser customUser) throws JsonProcessingException {
        if (approvalCertificates.size() > 10)
            throw ExceptionFactory.customValidationError("Upload maximum upto 10 files");

        approvalService.uploadApprovals(eventId, customUser.getId(), customUser.getOrganizationId(),
                approvalCertificates, approvalsInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CustomApiResponse<>(null, "certificates for approvals uploaded successfully", true)
        );

    }

    @PostMapping(path = "/approve")
    public ResponseEntity<CustomApiResponse<Void>> approveCertificates(@Valid @RequestBody ApproveCertificatesRequest
                                                                                   approveCertificatesRequest,
                                                                       @AuthenticationPrincipal CustomUser customUser) {

        approvalService.approveCertificates
                (customUser.getId(),customUser.getOrganizationId(),approveCertificatesRequest.getApprovalCertificateIds());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "check", true));
    }
}
