package com.credaegis.backend.controller;


import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.repository.CertificateRepository;
import com.credaegis.backend.service.CertificateService;
import lombok.AllArgsConstructor;
import okhttp3.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.ROUTEV1 + "/certificate-control")
@AllArgsConstructor
public class CertificateController {


    private final CertificateService certificateService;

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
