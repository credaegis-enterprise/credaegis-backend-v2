package com.credaegis.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateVerificationInfoDTO {

    private String certificateId;
    private String certificateName;
    private Date issuedDate;
    private String recipientName;
    private String recipientEmail;
    private String eventName;
    private String clusterName;
    private String organizationName;
    private Boolean revoked;
    private Date revokedDate;
    private Date expiryDate;
    private String comments;


}
