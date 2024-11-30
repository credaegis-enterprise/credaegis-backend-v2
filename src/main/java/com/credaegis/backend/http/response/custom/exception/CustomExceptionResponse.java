package com.credaegis.backend.http.response.custom.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomExceptionResponse {

    private String message;
    private Boolean success;

}
