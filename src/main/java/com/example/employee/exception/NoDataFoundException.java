package com.example.employee.exception;

public class NoDataFoundException extends Exception{

    private String message;

    public NoDataFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
