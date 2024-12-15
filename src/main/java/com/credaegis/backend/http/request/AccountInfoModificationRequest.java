package com.credaegis.backend.http.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountInfoModificationRequest {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Organization Name cannot be empty")
    private String organizationName;
}
