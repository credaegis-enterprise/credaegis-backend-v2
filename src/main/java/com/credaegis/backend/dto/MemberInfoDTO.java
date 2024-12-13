package com.credaegis.backend.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class MemberInfoDTO {

    private String username;
    private String email;
    private Boolean deactivated;
    private Timestamp createdOn;
}
