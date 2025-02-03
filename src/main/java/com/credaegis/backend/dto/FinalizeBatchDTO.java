package com.credaegis.backend.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FinalizeBatchDTO {

    private String batchId;
    private boolean isFinalized;
}
