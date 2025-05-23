package com.credaegis.backend.controller.member;



import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.ViewApprovalDTO;
import com.credaegis.backend.dto.projection.ApprovalInfoProjection;
import com.credaegis.backend.entity.ApprovalStatus;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.request.ApprovalModificationRequest;
import com.credaegis.backend.http.request.ApprovalsIdRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.service.member.MemberApprovalService;
import com.credaegis.backend.service.organization.ApprovalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = Constants.ROUTE_V1_MEMBER + "/approval-control")
@AllArgsConstructor
public class MemberApprovalController {


    private final MemberApprovalService approvalService;



    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @PostMapping(path = "/blockchain/approve")
    public ResponseEntity<CustomApiResponse<Void>> approveBlock(@Valid @RequestBody ApprovalsIdRequest
                                                                        approvalsIdRequest, @RequestParam(required = false,defaultValue = "false") Boolean persist,
                                                                @AuthenticationPrincipal CustomUser customUser) throws IOException {

        approvalService.
                approveCertificatesBlockchain(customUser.getId(),customUser.getClusterId(),approvalsIdRequest.getApprovalCertificateIds(),persist);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Requests to approve are processing", true));
    }








    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @GetMapping(path = "/get-count")
    public ResponseEntity<CustomApiResponse<Map<String,Long>>> getCount(@RequestParam String status,
                                                                        @AuthenticationPrincipal CustomUser customUser) {

        Map<String, Long> count = approvalService.getCount( customUser.getClusterId(), ApprovalStatus.valueOf(status));
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(count, "count fetched", true)
        );
    }

    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @PutMapping(path = "/modify")
    public ResponseEntity<CustomApiResponse<Void>> modifyApproval(@RequestBody @Valid ApprovalModificationRequest approvalModificationRequest,
                                                                  @AuthenticationPrincipal CustomUser customUser) {

        approvalService.modifyApproval(approvalModificationRequest, customUser.getClusterId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "approval modified Success", true)
        );
    }




    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @GetMapping(path = "/cluster/get-all")
    public ResponseEntity<CustomApiResponse<List<ApprovalInfoProjection>>> getAllApprovals(@AuthenticationPrincipal CustomUser customUser) {

        List<ApprovalInfoProjection> approvalInfoRespons = approvalService.getAllApprovals(customUser.getClusterId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(approvalInfoRespons, "approvals fetched", true)
        );
    }


    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @GetMapping(path = "/event/get-all/{id}")
    public ResponseEntity<CustomApiResponse<List<ApprovalInfoProjection>>> getAllApprovalsByEvent(@PathVariable String id,
                                                                                                  @AuthenticationPrincipal CustomUser customUser) {

        List<ApprovalInfoProjection> approvals = approvalService.getAllEventApprovals(id, customUser.getClusterId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(approvals, "approvals fetched", true)
        );
    }

    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @GetMapping(path = "/view/{id}")
    public ResponseEntity<InputStreamResource> viewApproval(@PathVariable String id, @AuthenticationPrincipal CustomUser customUser) {

        ViewApprovalDTO viewApprovalDTO = approvalService.viewApprovalCertificate(id, customUser.getClusterId());
        InputStreamResource resource = new InputStreamResource(viewApprovalDTO.getApprovalFileStream());
        return ResponseEntity.status(HttpStatus.OK).header(
                        HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + viewApprovalDTO.getApprovalFileName())
                .header("Content-Security-Policy", "frame-ancestors 'self' http://localhost:3000")
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

        approvalService.uploadApprovals(eventId, customUser.getId(), customUser.getClusterId(),
                approvalCertificates, approvalsInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CustomApiResponse<>(null, "certificates for approvals uploaded successfully", true)
        );

    }


    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @PostMapping(path = "/approve")
    public ResponseEntity<CustomApiResponse<Void>> approveCertificates(@Valid @RequestBody ApprovalsIdRequest
                                                                               approvalsIdRequest,
                                                                       @AuthenticationPrincipal CustomUser customUser) {

        approvalService.approveCertificates
                (customUser.getId(), customUser.getClusterId(), approvalsIdRequest.getApprovalCertificateIds());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Requests to approve are processing", true));
    }


    @PreAuthorize("hasRole('CLUSTER_ADMIN')")
    @PutMapping(path = "/reject")
    public ResponseEntity<CustomApiResponse<Void>> rejectCertificates(@Valid @RequestBody ApprovalsIdRequest
                                                                              approvalsIdRequest,
                                                                      @AuthenticationPrincipal CustomUser customUser) {

        approvalService.rejectCertificates
                (customUser.getClusterId(), approvalsIdRequest.getApprovalCertificateIds());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Requests to reject are processing", true));
    }
}
