package com.credaegis.backend.dto.response.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
public class CustomExceptionResponse {

    private String message;
    private Boolean success;

}
