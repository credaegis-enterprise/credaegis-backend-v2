package com.credaegis.backend.exception.custom;

public  class ExceptionFactory {


    public static RuntimeException customValidationError(String message) throws CustomException{
        return new CustomException(message,400);
    }

    public static RuntimeException insufficentPermission() throws CustomException{
        return new CustomException("You have insufficient permission, access denied",403);
    }

    public static RuntimeException resourceNotFound() throws  CustomException{
        return new CustomException("Resource not found",404);
    }
}
