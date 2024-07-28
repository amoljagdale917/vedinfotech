package com.demo.vedinfotech.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorDetails {

    private String message;
    private String status;
    private int statusCode;
    private HttpStatus httpStatus;
    private String errorDetails;

    public ErrorDetails(String message, String status, int statusCode, HttpStatus httpStatus, String errorDetails) {
        this.message = message;
        this.status = status;
        this.statusCode = statusCode;
        this.httpStatus = httpStatus;
        this.errorDetails = errorDetails;
    }
}
