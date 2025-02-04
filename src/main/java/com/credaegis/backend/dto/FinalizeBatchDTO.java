package com.credaegis.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalizeBatchDTO {

    private String batchId;
    private Boolean isFinalized;
}
