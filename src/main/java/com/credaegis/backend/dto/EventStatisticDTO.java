package com.credaegis.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventStatisticDTO {

    private String name;
    private String clusterName;
    private Long issuedCertificateCount;
}
