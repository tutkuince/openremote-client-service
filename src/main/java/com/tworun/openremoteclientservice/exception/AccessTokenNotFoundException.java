package com.tworun.openremoteclientservice.exception;

public class AccessTokenNotFoundException extends RuntimeException {

    public AccessTokenNotFoundException(String message) {
        super(message);
    }
}
