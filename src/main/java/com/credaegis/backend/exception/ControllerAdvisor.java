package com.credaegis.backend.exception;

import com.credaegis.backend.dto.response.exception.CustomExceptionResponse;
import com.credaegis.backend.exception.custom.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception)
            throws MethodArgumentNotValidException{

        String message = exception.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        CustomExceptionResponse response = new CustomExceptionResponse(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomExceptionResponse> constraintViolationException(ConstraintViolationException exception)
            throws ConstraintViolationException{

        String message = new ArrayList<>(exception.getConstraintViolations()).getFirst().getMessage();
        CustomExceptionResponse response = new CustomExceptionResponse(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomExceptionResponse> customException(CustomException exception)
        throws CustomException{

        CustomExceptionResponse response = new CustomExceptionResponse(exception.getMessage());
        return ResponseEntity.status(exception.getErrorCode()).body(response);
    }
}
