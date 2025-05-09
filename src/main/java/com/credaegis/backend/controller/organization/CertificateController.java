package com.credaegis.backend.controller.organization;


import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.ViewCertificateDTO;
import com.credaegis.backend.dto.projection.CertificateInfoProjection;
import com.credaegis.backend.http.request.CertificateRevokeRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.http.response.custom.CertificateListResponse;
import com.credaegis.backend.service.organization.CertificateService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = Constants.ROUTE_V1_ORGANIZATION + "/certificate-control")
@AllArgsConstructor
public class CertificateController {


    private final CertificateService certificateService;



    @GetMapping("/{hash}")
    public ResponseEntity<CustomApiResponse<CertificateInfoProjection>> getDetailsByHash(@PathVariable String hash,
                                                                                         @AuthenticationPrincipal CustomUser customUser)
    {
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(
                        certificateService.getInfoByHash(hash,customUser.getOrganizationId()),
                        null,true
                )
        );
    }


    @GetMapping(path = "/view/{id}")
    public ResponseEntity<InputStreamResource> viewApproval(@PathVariable String id, @AuthenticationPrincipal CustomUser customUser) {

        ViewCertificateDTO viewCertificateDTO = certificateService.viewCertificate(id,customUser.getOrganizationId());
        InputStreamResource resource = new InputStreamResource(viewCertificateDTO.getCertificateFileStream());
        return ResponseEntity.status(HttpStatus.OK).header(
                        HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + viewCertificateDTO.getCertificateFileName())
                .header("Content-Security-Policy", "frame-ancestors 'self' http://localhost:3000")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);

    }

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
