package com.demo.vedinfotech.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiResponse<T> {
    private String status;
    private int statusCode;
    private String description;
    private HttpStatus httpStatus;
    private T data;

    public ApiResponse(String status, int statusCode, String description, HttpStatus httpStatus, T data) {
        this.status = status;
        this.statusCode = statusCode;
        this.description = description;
        this.httpStatus = httpStatus;
        this.data = data;
    }
}
