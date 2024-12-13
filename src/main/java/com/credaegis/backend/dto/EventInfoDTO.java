package com.credaegis.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class EventInfoDTO {

    private String id;
    private String name;
    private String description;
    private Boolean deactivated;
    private Timestamp createdOn;
}
