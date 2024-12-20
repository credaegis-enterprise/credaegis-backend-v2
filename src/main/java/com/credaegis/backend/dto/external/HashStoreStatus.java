package com.credaegis.backend.dto.external;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HashStoreStatus {

    @JsonProperty("Hash")
    private String hash;

    @JsonProperty("Stored")
    private boolean stored;
}
