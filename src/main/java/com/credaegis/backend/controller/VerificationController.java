package com.credaegis.backend.controller;

import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.CertificateVerificationResponse;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.service.VerificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = Constants.ROUTEV1 + "/external")
@AllArgsConstructor
public class VerificationController {

    private VerificationService verificationService;

    @GetMapping(path = "/verify")
    public ResponseEntity<CustomApiResponse<List<CertificateVerificationResponse>>> verifyCertificates(@RequestParam("certificates")
                                                                                                       List<MultipartFile> multipartFiles)
            throws IOException {


        if(multipartFiles.size() > 10)
            throw ExceptionFactory.customValidationError("Can't process more than 10 files");
        List<CertificateVerificationResponse> certificateVerificationResponseList =
                verificationService.verifyAuthenticity(multipartFiles);

        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(certificateVerificationResponseList,"verification result",true)
        );

    }
}
