package com.credaegis.backend.dto.projection;

public interface EventInfoProjection {

    public String getId();
    public String getName();
    public String getDescription();
    public Boolean getDeactivated();
    public String getCreatedOn();
    public String getUpdatedOn();
}
