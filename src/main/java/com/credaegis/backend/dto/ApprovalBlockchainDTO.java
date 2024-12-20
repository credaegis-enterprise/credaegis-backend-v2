package com.credaegis.backend.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ApprovalBlockchainDTO {

    private String approvalId;
    private String hash;
    private Boolean stored;
    private String userId;

}
