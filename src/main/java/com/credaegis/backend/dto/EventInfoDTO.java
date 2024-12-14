package com.credaegis.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventInfoDTO {

    private String id;
    private String name;
    private String description;
    private Boolean deactivated;
    private Timestamp createdOn;
}
