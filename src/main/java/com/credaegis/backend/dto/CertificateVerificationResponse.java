package com.credaegis.backend.dto;


import com.credaegis.backend.http.response.custom.CertificateVerificationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CertificateVerificationResponse {


    private  String certificateName;
    private Boolean isIssued;
    private CertificateVerificationInfo certificateVerificationInfo;

}


