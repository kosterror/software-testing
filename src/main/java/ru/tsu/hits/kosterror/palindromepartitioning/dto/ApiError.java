package ru.tsu.hits.kosterror.palindromepartitioning.dto;

import java.time.LocalDateTime;

public class ApiError {

    private String method;
    private String path;
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;

    public ApiError(String method, String path, int statusCode, String message, LocalDateTime timestamp) {
        this.method = method;
        this.path = path;
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
