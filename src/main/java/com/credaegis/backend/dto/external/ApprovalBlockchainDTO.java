package com.credaegis.backend.dto.external;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ApprovalBlockchainDTO {

     private  String userId;
     private List<String> hashes;

}
