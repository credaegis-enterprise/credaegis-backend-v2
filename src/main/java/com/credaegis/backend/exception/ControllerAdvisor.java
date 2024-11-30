package com.credaegis.backend.exception;

import com.credaegis.backend.http.response.custom.exception.CustomExceptionResponse;
import com.credaegis.backend.exception.custom.CustomException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception)
            throws MethodArgumentNotValidException{

        String message = exception.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        CustomExceptionResponse response = new CustomExceptionResponse(message,false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomExceptionResponse> constraintViolationException(ConstraintViolationException exception)
            throws ConstraintViolationException{

        String message = new ArrayList<>(exception.getConstraintViolations()).getFirst().getMessage();
        CustomExceptionResponse response = new CustomExceptionResponse(message,false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomExceptionResponse> customException(CustomException exception)
        throws CustomException{

        CustomExceptionResponse response = new CustomExceptionResponse(exception.getMessage(),false);
        return ResponseEntity.status(exception.getHttpStatus()).body(response);
    }
}
