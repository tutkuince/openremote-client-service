package com.tworun.openremoteclientservice.exception;

/**
 * Exception thrown when an access token cannot be obtained from the Auth server.
 */
public class AccessTokenNotFoundException extends RuntimeException {

    public AccessTokenNotFoundException(String message) {
        super(message);
    }
}
