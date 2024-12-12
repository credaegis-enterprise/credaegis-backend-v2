package com.credaegis.backend.http.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ApprovalsIdRequest {

    @NotEmpty(message = "Approval List cannot be empty")
    private List<String> approvalCertificateIds;

}
