package com.credaegis.backend.dto.projection;

import java.sql.Timestamp;

public interface MemberInfoProjection {

    public String getId();
    public String getUsername();
    public String getEmail();
    public Boolean getDeactivated();
    public Timestamp getCreatedOn();
    public Timestamp getUpdatedOn();
}
