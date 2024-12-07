package com.credaegis.backend.http.response.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomExceptionResponse {

    private String message;
    private Boolean success;

}
