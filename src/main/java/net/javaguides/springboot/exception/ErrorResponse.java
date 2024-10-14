package net.javaguides.springboot.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final String message;
    private final String status;
    private final LocalDateTime timestamp;

    public ErrorResponse(String message, String status, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
