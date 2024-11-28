package com.credaegis.backend.exception.custom;

import org.springframework.http.HttpStatus;

public class CustomException  extends RuntimeException {

    private HttpStatus httpStatus;
    public CustomException(String message,HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }



    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
