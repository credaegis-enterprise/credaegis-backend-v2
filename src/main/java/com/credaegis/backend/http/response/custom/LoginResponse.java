package com.credaegis.backend.http.response.custom;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {

    private final boolean mfaEnabled;
    private final String role;
    private final String accountType;
}
