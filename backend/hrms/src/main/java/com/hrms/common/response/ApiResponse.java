package com.hrms.common.response;

import java.time.LocalDateTime;

public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private int status;
    private String path;
    private LocalDateTime timestamp;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, String message, T data, int status, String path) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
    
    public static <T> ApiResponse<T> error(String message, int status, String path) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        response.status = status;
        response.path = path;
        response.timestamp = java.time.LocalDateTime.now();
        return response;
    }

    public boolean isSuccess() { return success; }

    public String getMessage() { return message; }

    public T getData() { return data; }

    public int getStatus() { return status; }

    public String getPath() { return path; }

    public LocalDateTime getTimestamp() { return timestamp; }
}