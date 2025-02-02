package com.credaegis.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Web3InfoDTO {


    private String networkName;
    private String networkId;
    private String clientVersion;
    private String balance;
}
