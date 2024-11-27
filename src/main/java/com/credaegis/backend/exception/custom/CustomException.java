package com.credaegis.backend.exception.custom;

public class CustomException  extends RuntimeException {

    private Integer errorCode;
    public CustomException(String message,Integer errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
