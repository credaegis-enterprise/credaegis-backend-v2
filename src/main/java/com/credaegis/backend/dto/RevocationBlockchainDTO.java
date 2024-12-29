package com.credaegis.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevocationBlockchainDTO {
    private String certificateId;
    private String revokerId;
    private Boolean revoked;
    private String hash;
}
