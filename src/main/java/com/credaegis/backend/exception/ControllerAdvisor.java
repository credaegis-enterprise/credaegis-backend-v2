package com.credaegis.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidException(Exception exception)
            throws MethodArgumentNotValidException{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("constraint failure");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> constraintViolationException(Exception exception)
            throws ConstraintViolationException{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("constraint failure");
    }
}
