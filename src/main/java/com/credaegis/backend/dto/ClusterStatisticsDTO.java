package com.credaegis.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClusterStatisticsDTO {

    private String name;
    private Long issuedCertificateCount;
    private Long revokedCertificateCount;
    private Long rejectedCertificateCount;
}
