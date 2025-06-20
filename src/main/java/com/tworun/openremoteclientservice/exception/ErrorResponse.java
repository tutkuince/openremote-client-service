package com.tworun.openremoteclientservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String code;
    private String message;

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
}
