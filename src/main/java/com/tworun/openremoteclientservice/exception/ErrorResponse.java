package com.tworun.openremoteclientservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorResponse {
    private String code;
    private String message;
    private List<String> details;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public ErrorResponse() {
        this.createdAt = LocalDateTime.now();
    }

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public ErrorResponse(String code, String message, List<String> details) {
        this.code = code;
        this.message = message;
        this.details = details;
        this.createdAt = LocalDateTime.now();
    }
}
