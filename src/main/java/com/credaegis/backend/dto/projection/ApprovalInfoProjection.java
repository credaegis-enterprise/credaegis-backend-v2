package com.credaegis.backend.dto.projection;

import com.credaegis.backend.entity.ApprovalStatus;

import java.sql.Date;
import java.sql.Timestamp;

public interface ApprovalInfoProjection {

    public String getId();
    public String getApprovalCertificateName();
    public String getRecipientName();
    public String getRecipientEmail();
    public Date getExpiryDate();
    public String getComment();
    public ApprovalStatus getStatus();
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
