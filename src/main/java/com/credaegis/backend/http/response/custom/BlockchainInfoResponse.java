package com.credaegis.backend.http.response.custom;


import com.credaegis.backend.dto.HashBatchInfoDTO;
import com.credaegis.backend.dto.Web3InfoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockchainInfoResponse {


    @JsonProperty("web3Info")
    private Web3InfoDTO web3InfoDTO;

    @JsonProperty("currentBatchInfo")
    private HashBatchInfoDTO hashBatchInfoDTO;
}