package com.credaegis.backend.http.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClusterCreationRequest {

    @NotBlank(message = "name cannot be empty")
    private String clusterName;

    @NotBlank(message = "name cannot be empty")
    private String adminName;

    @Email(message = "Please enter a valid email")
    private String adminEmail;
}
