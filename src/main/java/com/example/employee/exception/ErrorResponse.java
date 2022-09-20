package com.example.employee.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private LocalDateTime timeStamp;
    private String message;
    private Map<String, String> fieldErrors = new HashMap<>();

    public ErrorResponse(){}

    public ErrorResponse(LocalDateTime timeStamp, String message,  Map<String, String> fieldErrors) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "timeStamp=" + timeStamp +
                ", message='" + message + '\'' +
                ", fieldErrors=" + fieldErrors +
                '}';
    }
}
