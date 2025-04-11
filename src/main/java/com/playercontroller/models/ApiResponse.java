package com.playercontroller.models;

import lombok.Data;
import java.util.List;

@Data
public class ApiResponse<T> {
    private T data;
    private Integer statusCode;
    private List<ErrorResponse> errorMessages;

    public ApiResponse(T data, Integer statusCode, List<ErrorResponse> errorMessages) {
        this.data = data;
        this.statusCode = statusCode;
        this.errorMessages = errorMessages;
    }
}