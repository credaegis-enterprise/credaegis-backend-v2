package com.credaegis.backend.http.response.custom;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class CertificateVerificationResponse {


    private  String certificateName;
    private Boolean isIssued;
    private Info info;

}


@Data
@NoArgsConstructor
class Info {

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
    private String comments;


}
