package com.credaegis.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClusterInfoDTO {
    private String id;
    private String name;
    private Boolean locked;
    private Boolean deactivated;
    private Timestamp createdOn;

}
