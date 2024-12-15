package com.credaegis.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClusterStatisticsDTO {

    private String name;
    private Long issuedCertificateCount;
    private Long revokedCertificateCount;
    private Long rejectedCertificateCount;
}
