package com.credaegis.backend.exception.custom;

import org.springframework.http.HttpStatus;

public  class ExceptionFactory {



    public static RuntimeException accessDeniedException(String message) throws CustomException{
        return new CustomException(message,HttpStatus.FORBIDDEN);
    }
    public  static RuntimeException customValidationError(String message) throws CustomException{
        return new CustomException(message, HttpStatus.BAD_REQUEST);
    }

    public static RuntimeException insufficientPermission() throws CustomException{
        return new CustomException("You have insufficient permission, access denied",HttpStatus.UNAUTHORIZED);
    }


    public static RuntimeException resourceNotFound() throws  CustomException{
        return new CustomException("Resource not found",HttpStatus.NOT_FOUND);
    }
}
