package com.credaegis.backend.controller;


import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.ViewApprovalDTO;
import com.credaegis.backend.entity.Approval;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.request.ApprovalsIdRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.http.response.custom.ApprovalInfoResponse;
import com.credaegis.backend.service.ApprovalService;
import com.credaegis.backend.utility.CheckSumUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping(value = Constants.ROUTEV1 + "/approval-control")
@AllArgsConstructor
public class ApprovalController {


    private final ApprovalService approvalService;
    private final CheckSumUtility checkSumUtility;


    @GetMapping(path = "/get-all")
    public ResponseEntity<CustomApiResponse<List<ApprovalInfoResponse>>> getAllApprovals(@AuthenticationPrincipal CustomUser customUser) {

        List<ApprovalInfoResponse> approvalInfoResponses= approvalService.getAllApprovals(customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(approvalInfoResponses, "approvals fetched", true)
        );
    }

    @GetMapping(path = "/cluster/get-all/{id}")
    public ResponseEntity<CustomApiResponse<List<Approval>>> getAllApprovalsByCluster(@PathVariable String id,
                                                                                      @AuthenticationPrincipal CustomUser customUser) {

        List<Approval> approvals = approvalService.getAllClusterApprovals(id, customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(approvals, "approvals fetched", true)
        );
    }

    @GetMapping(path = "/event/get-all/{id}")
    public ResponseEntity<CustomApiResponse<List<Approval>>> getAllApprovalsByEvent(@PathVariable String id,
                                                                                    @AuthenticationPrincipal CustomUser customUser) {

        List<Approval> approvals = approvalService.getAllEventApprovals(id, customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(approvals, "approvals fetched", true)
        );
    }


    @GetMapping(path = "/view/{id}")
    public ResponseEntity<InputStreamResource> viewApproval(@PathVariable String id, @AuthenticationPrincipal CustomUser customUser) {

        ViewApprovalDTO viewApprovalDTO = approvalService.viewApprovalCertificate(id, customUser.getOrganizationId());
        InputStreamResource resource = new InputStreamResource(viewApprovalDTO.getApprovalFileStream());
        return ResponseEntity.status(HttpStatus.OK).header(
                        HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + viewApprovalDTO.getApprovalFileName())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);

    }

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
    public ResponseEntity<CustomApiResponse<Void>> approveCertificates(@Valid @RequestBody ApprovalsIdRequest
                                                                               approvalsIdRequest,
                                                                       @AuthenticationPrincipal CustomUser customUser) {

        approvalService.approveCertificates
                (customUser.getId(), customUser.getOrganizationId(), approvalsIdRequest.getApprovalCertificateIds());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Requests to approve are processing", true));
    }

    @PutMapping(path = "/reject")
    public ResponseEntity<CustomApiResponse<Void>> rejectCertificates(@Valid @RequestBody ApprovalsIdRequest
                                                                              approvalsIdRequest,
                                                                      @AuthenticationPrincipal CustomUser customUser) {

        approvalService.rejectCertificates
                (customUser.getOrganizationId(), approvalsIdRequest.getApprovalCertificateIds());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Requests to reject are processing", true));
    }
}
