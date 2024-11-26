package com.credaegis.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClusterCreationRequest {

    @NotBlank
    private String clusterName;

    @NotBlank
    private String adminName;

    @Email(message = "Please enter a valid email")
    private String adminEmail;
}
