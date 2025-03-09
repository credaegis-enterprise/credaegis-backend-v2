package com.credaegis.backend.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HashMerkleRootDTO {
    private String hash;
    private String merkleRoot;
}
