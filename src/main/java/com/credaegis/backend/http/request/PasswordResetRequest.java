package com.credaegis.backend.http.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordResetRequest {

    private String newPassword;
    private String resetToken;
    private String email;
}
