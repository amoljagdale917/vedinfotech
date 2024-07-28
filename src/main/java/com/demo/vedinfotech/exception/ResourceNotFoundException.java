package com.demo.vedinfotech.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String status;
    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String errorDetails;

    public ResourceNotFoundException(String message, String status, int statusCode, HttpStatus httpStatus, String errorDetails) {
        super(message);
        this.status = status;
        this.statusCode = statusCode;
        this.httpStatus = httpStatus;
        this.errorDetails = errorDetails;
    }
}
