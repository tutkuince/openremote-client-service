package com.tworun.openremoteclientservice.exception;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Handles exceptions globally across all controllers.
 * <p>
 * Provides consistent error responses for authentication, validation, resource not found, and other unexpected errors.
 * Logs error details for troubleshooting.
 * <p>
 * Example error codes:
 * <ul>
 *     <li>AUTH_ERROR</li>
 *     <li>AUTH_TOKEN_NOT_FOUND</li>
 *     <li>VALIDATION_ERROR</li>
 *     <li>ASSET_NOT_FOUND</li>
 *     <li>INTERNAL_ERROR</li>
 * </ul>
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles authentication-related exceptions.
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException exception) {
        ErrorResponse response = new ErrorResponse("AUTH_ERROR", exception.getMessage());
        log.error("AuthException occurred: {}", response, exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handles cases where an access token could not be found or obtained.
     */
    @ExceptionHandler(AccessTokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccessTokenNotFoundException(AccessTokenNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("AUTH_TOKEN_NOT_FOUND", ex.getMessage());
        log.error("AccessTokenNotFoundException occurred: {}", errorResponse, ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles validation errors thrown by @Valid.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION_ERROR",
                "Validation failed",
                errors
        );

        log.warn("Validation error: {}", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles cases where an asset could not be found.
     */
    @ExceptionHandler(AssetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAssetNotFound(AssetNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("ASSET_NOT_FOUND", ex.getMessage());
        log.warn("AssetNotFoundException: {}", error, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<ErrorResponse> handleRequestNotPermittedException(RequestNotPermitted ex) {
        log.warn("Rate limit exceeded for a request: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "RATE_LIMIT_EXCEEDED",
                "You have exceeded the API rate limit. Please try again later."
                );
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    /**
     * Handles any unexpected exceptions not explicitly handled above.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred.");
        log.error("Unexpected error: {}", errorResponse, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
