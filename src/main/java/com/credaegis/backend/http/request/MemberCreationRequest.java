package com.credaegis.backend.http.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberCreationRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "Cluster Id cannot be blank")
    private String clusterId;
}
