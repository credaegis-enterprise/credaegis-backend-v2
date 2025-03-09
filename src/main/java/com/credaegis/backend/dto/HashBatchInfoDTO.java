package com.credaegis.backend.dto;


import com.credaegis.backend.entity.BatchInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HashBatchInfoDTO {


    private String batchId;
    private List<String> hashes;
    private String merkleRoot;
    private BatchInfo batchInfo;

}