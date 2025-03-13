package com.credaegis.backend.dto.projection;

import com.credaegis.backend.entity.CertificateStatus;

import java.sql.Date;

public interface CertificateInfoProjection {

    public String getId();
    public String getRecipientName();
    public String getRecipientEmail();
    public String getIssuerName();
    public String getIssuerEmail();
    public Date getIssuedDate();
    public Date getExpiryDate();
    public Boolean getPersisted();
    public Boolean getRevoked();
    public Date getRevokedDate();
    public String getComments();
    public String getCertificateName();
    public String getEventName();
    public String getClusterName();
    public CertificateStatus getStatus();

    
    //for frontend
    default Boolean getSelected(){
        return false;
    }


}
