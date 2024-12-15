package com.credaegis.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventStatisticDTO {

    private String name;
    private String clusterName;
    private Long issuedCertificateCount;
}
