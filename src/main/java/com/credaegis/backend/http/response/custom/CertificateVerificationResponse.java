package com.credaegis.backend.http.response.custom;


import com.credaegis.backend.dto.CertificateVerificationInfoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CertificateVerificationResponse {


    private String certificateName;
    private Boolean isIssued;
    private Boolean infoFound;
    private Boolean isPublicVerified;

    @JsonProperty("info")
    private CertificateVerificationInfoDTO certificateVerificationInfoDTO;

}


