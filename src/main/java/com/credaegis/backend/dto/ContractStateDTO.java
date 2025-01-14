package com.credaegis.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractStateDTO {

    private String currentBatchIndex;
    private String batchHashCount;

}
