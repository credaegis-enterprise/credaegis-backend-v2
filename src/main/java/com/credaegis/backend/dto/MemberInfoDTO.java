package com.credaegis.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberInfoDTO {

    private String id;
    private String username;
    private String email;
    private Boolean deactivated;
    private Timestamp createdOn;
}
