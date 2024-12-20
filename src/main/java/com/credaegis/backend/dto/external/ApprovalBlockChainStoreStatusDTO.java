package com.credaegis.backend.dto.external;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ApprovalBlockChainStoreStatusDTO {
    private String userId;
    List<HashStoreStatus> hashesStored;
}



