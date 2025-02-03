package com.credaegis.backend.http.request;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MerkleRootVerificationRequest {
    private List<String> merkleRoots;
}
