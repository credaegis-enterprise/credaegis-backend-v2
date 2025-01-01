package com.credaegis.backend.controller;


import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.CertificateInfoDTO;
import com.credaegis.backend.dto.projection.CertificateInfoProjection;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.http.request.CertificateRevokeRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.http.response.custom.CertificateListResponse;
import com.credaegis.backend.repository.CertificateRepository;
import com.credaegis.backend.service.CertificateService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import okhttp3.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = Constants.ROUTEV1 + "/certificate-control")
@AllArgsConstructor
public class CertificateController {


    private final CertificateService certificateService;


    @GetMapping(path = "/issued/get-count")
    public ResponseEntity<CustomApiResponse<Map<String,Long>>> getTotalCount(@AuthenticationPrincipal CustomUser customUser){

            return ResponseEntity.status(HttpStatus.OK).body(
                    new CustomApiResponse<>(
                            certificateService.getTotalIssuedCertificateCount(customUser.getOrganizationId()),
                            "total issued count",true
                    )
            );
    }

    @PutMapping(path = "/blockchain/revoke")
    public ResponseEntity<CustomApiResponse<Void>> revokeCertitficatesBlockchain(@RequestBody @Valid CertificateRevokeRequest certificateRevokeRequest,
                                                                                 @AuthenticationPrincipal CustomUser customUser) {

        certificateService.revokeCertificatesBlockchain(certificateRevokeRequest.getCertificateIds(),customUser.getOrganizationId(),
                customUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new CustomApiResponse<>(null,"processing revocation",true));
    }



    @PutMapping(path = "/revoke")
    public ResponseEntity<CustomApiResponse<Void>> revokeCertificates(@RequestBody @Valid CertificateRevokeRequest certificateRevokeRequest,
                                                                      @AuthenticationPrincipal CustomUser customUser) {

        certificateService.revokeCertificates(certificateRevokeRequest.getCertificateIds(), customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(new CustomApiResponse<>(null, "Certificates revoked", true));

    }

    @GetMapping(path="/cluster/{id}/get-latest")
    public ResponseEntity<CustomApiResponse<CertificateListResponse>> getLatestCertificatesCluster(@RequestParam("page") int page,
                                                                                                   @RequestParam("size") int size,
                                                                                                   @PathVariable String id,
                                                                                                   @AuthenticationPrincipal CustomUser customUser) {
        CertificateListResponse certificateListResponse = new CertificateListResponse();
        certificateListResponse.setCount(certificateService.getIssuedCountCluster(customUser.getOrganizationId(),id));
        certificateListResponse.setCertificateInfoProjection(certificateService.getLatestCertificatesCluster(page,size, customUser.getOrganizationId(), id));
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(
                        certificateListResponse,
                        null,true
                )
        );

    }

    @GetMapping(path="/event/{id}/get-latest")
    public ResponseEntity<CustomApiResponse<CertificateListResponse>> getLatestCertificatesEvent(@RequestParam("page") int page,
                                                                                           @RequestParam("size") int size,
                                                                                           @PathVariable String id,
                                                                                           @AuthenticationPrincipal CustomUser customUser) {
        CertificateListResponse certificateListResponse = new CertificateListResponse();
        certificateListResponse.setCount(certificateService.getIssuedCountEvent(customUser.getOrganizationId(),id));
        certificateListResponse.setCertificateInfoProjection(certificateService.getLatestCertificatesEvent(page,size, customUser.getOrganizationId(), id));
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(
                        certificateListResponse,
                        null,true
                )
        );

    }

    @GetMapping(path = "/get-latest")
    public ResponseEntity<CustomApiResponse<CertificateListResponse>> getLatestCertificates(@RequestParam("page") int page,
                                                                                             @RequestParam("size") int size,
                                                                                                    @AuthenticationPrincipal CustomUser customUser) {

        CertificateListResponse certificateListResponse = new CertificateListResponse();
        certificateListResponse.setCertificateInfoProjection(certificateService.getLatestCertificates(page,size, customUser.getOrganizationId()));
        certificateListResponse.setCount(certificateService.getTotalIssuedCertificateCount(customUser.getOrganizationId()).get("count"));
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(
                        certificateListResponse,
                        null,true
                )
        );

    }
}
