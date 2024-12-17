package com.credaegis.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateInfoDTO {

    private String id;
    private String recipientName;
    private String recipientEmail;
    private String issuerName;
    private String issuerEmail;
    private Date issuedDate;
    private Date expiryDate;
    private Boolean revoked;
    private Date revokedDate;
    private String comments;
    private String certificateName;
    private String eventName;
    private String clusterName;

}
