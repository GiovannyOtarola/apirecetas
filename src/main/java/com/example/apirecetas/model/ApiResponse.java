package com.example.apirecetas.model;

import lombok.Data;

@Data
public class ApiResponse {

    private String message;
    private boolean success;


    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
