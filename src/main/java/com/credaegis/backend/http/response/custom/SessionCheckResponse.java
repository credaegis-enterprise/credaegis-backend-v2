package com.credaegis.backend.http.response.custom;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionCheckResponse {

    private  String role;
    private  String accountType;
}
