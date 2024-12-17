package com.credaegis.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateStatisticsDTO {

    private Long issuedCertificateCount;
    private Long expiredCertificateCount;
    private Long revokedCertificateCount;
    private Long rejectedCertificateCount;

}
