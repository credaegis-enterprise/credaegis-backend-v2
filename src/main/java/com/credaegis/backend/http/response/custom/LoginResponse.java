package com.credaegis.backend.http.response.custom;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private final boolean mfaEnabled;
    private final String role;
    private final String accountType;
}
