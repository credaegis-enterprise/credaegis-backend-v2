package com.credaegis.backend.http.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordChangeRequest {

    @NotBlank(message = "Old password should not be empty")
    private String oldPassword;

    @NotBlank(message = "Password should not be empty")
    private String newPassword;

    @NotBlank(message = "Should not be empty")
    private String confirmPassword;



}
