package com.credaegis.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateVerificationBlockchainResultDTO {

    private String hash;
    private Boolean isVerified;
}
