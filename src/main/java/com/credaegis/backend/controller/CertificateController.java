package com.credaegis.backend.controller;


import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.http.request.CertificateRevokeRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
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

@RestController
@RequestMapping(value = Constants.ROUTEV1 + "/certificate-control")
@AllArgsConstructor
public class CertificateController {


    private final CertificateService certificateService;


    @PutMapping(path = "/revoke")
    public ResponseEntity<CustomApiResponse<Void>> revokeCertificates(@RequestBody @Valid CertificateRevokeRequest certificateRevokeRequest,
                                                                      @AuthenticationPrincipal CustomUser customUser) {

        certificateService.revokeCertificates(certificateRevokeRequest.getCertificateIds(), customUser.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(new CustomApiResponse<>(null, "Certificates revoked", true));

    }

    @GetMapping(path="/cluster/{id}/get-latest")
    public ResponseEntity<CustomApiResponse<Page<Certificate>>> getLatestCertificatesCluster(@RequestParam("page") int page,
                                                                                             @RequestParam("size") int size,
                                                                                             @PathVariable String id,
                                                                                             @AuthenticationPrincipal CustomUser customUser) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(
                        certificateService.getLatestCertificatesCluster(page,size, customUser.getOrganizationId(), id),
                        null,true
                )
        );

    }

    @GetMapping(path="/event/{id}/get-latest")
    public ResponseEntity<CustomApiResponse<Page<Certificate>>> getLatestCertificatesEvent(@RequestParam("page") int page,
                                                                                           @RequestParam("size") int size,
                                                                                           @PathVariable String id,
                                                                                           @AuthenticationPrincipal CustomUser customUser) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(
                        certificateService.getLatestCertificatesEvent(page,size, customUser.getOrganizationId(), id),
                        null,true
                )
        );

    }

    @GetMapping(path = "/get-latest")
    public ResponseEntity<CustomApiResponse<Page<Certificate>>> getLatestCertificates(@RequestParam("page") int page,
                                                                                      @RequestParam("size") int size,
                                                                                      @AuthenticationPrincipal CustomUser customUser) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(
                        certificateService.getLatestCertificates(page,size, customUser.getOrganizationId()),
                        null,true
                )
        );

    }
}
