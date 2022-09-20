package com.example.employee.exception;

public class DepartmentNotFoundException extends Exception{

    private String message;

    public DepartmentNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
