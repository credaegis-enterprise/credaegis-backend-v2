package com.credaegis.backend.http.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordResetRequest {


    @NotBlank(message = "new password cannot be empty")
    private String newPassword;

    @NotBlank(message = "confirm password cannot be empty")
    private String confirmPassword;

    @NotBlank(message = "reset token cannot be empty")
    private String resetToken;

    @Email(message = "email cannot be empty")
    private String email;
}
