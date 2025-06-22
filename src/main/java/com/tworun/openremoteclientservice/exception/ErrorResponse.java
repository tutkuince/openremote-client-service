package com.tworun.openremoteclientservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response structure for API endpoints.
 * <p>
 * This class is used to return consistent error details for failed requests,
 * including an error code, a user-friendly message, and additional details when applicable.
 */
@Schema(name = "ErrorResponse", description = "Model representing error response for failed requests.")
@Data
public class ErrorResponse {
    @Schema(description = "Error code.", example = "VALIDATION_ERROR")
    private String code;

    @Schema(description = "Error message.", example = "Validation failed")
    private String message;

    @Schema(description = "Validation error details, if any.")
    private List<String> details;

    @Schema(description = "Error timestamp (ISO format).", example = "2025-06-22T16:19:34")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

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
