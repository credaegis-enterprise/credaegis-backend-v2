package com.credaegis.backend.http.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CertificateRevokeRequest {

    @NotEmpty(message = "Certificate List to revoke cannot be empty")
    List<String> certificateIds;
}
