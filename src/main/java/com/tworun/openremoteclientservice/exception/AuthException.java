package com.tworun.openremoteclientservice.exception;

/**
 * Exception thrown when an authentication error occurs while obtaining or using an access token.
 * <p>
 * This exception is typically thrown when communication with the authentication server fails,
 * or when the token cannot be retrieved or validated.
 */
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
