package com.skillsphere.common;

import java.time.LocalDateTime;

public class ApiResponse {

    private LocalDateTime timestamp;
    private boolean success;
    private String message;
    private Object data;

    public ApiResponse() {
    }

    public ApiResponse(
            boolean success,
            String message,
            Object data) {

        this.timestamp = LocalDateTime.now();
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}