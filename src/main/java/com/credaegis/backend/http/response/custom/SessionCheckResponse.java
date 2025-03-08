package com.credaegis.backend.http.response.custom;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SessionCheckResponse {

    private  String role;
    private  String accountType;
}
