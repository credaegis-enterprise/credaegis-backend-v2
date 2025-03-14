package com.credaegis.backend.http.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClusterCreationRequest {

    @NotBlank(message = "name cannot be empty")
    @Size(max = 20, message = "cluster name should not exceed 50 characters")
    private String clusterName;

    @NotBlank(message = "name cannot be empty")
    @Size(max = 20, message = "admin name should not exceed 50 characters")
    private String adminName;

    @Email(message = "Please enter a valid email")
    @NotBlank(message = "email cannot be empty")
    @Size(max = 50, message = "email should not exceed 50 characters")
    private String adminEmail;
}
