package com.credaegis.backend.http.response.custom;

import com.credaegis.backend.entity.Status;

import java.sql.Date;
import java.sql.Timestamp;

public interface ApprovalInfoResponse {

    public String getId();
    public String getApprovalCertificateName();
    public String getRecipientName();
    public String getRecipientEmail();
    public Date getExpiryDate();
    public String getComment();
    public Status getStatus();
    public Timestamp getCreatedOn();
    public Timestamp getUpdatedOn();
    public String getEventId();
    public String getEventName();
    public String getClusterId();
    public String getClusterName();
    public String getOrganizationName();
    default Boolean getSelected(){
        return false;
    }
}
