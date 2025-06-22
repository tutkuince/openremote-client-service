package com.tworun.openremoteclientservice.exception;

/**
 * Exception thrown when an asset with the specified ID cannot be found.
 */
public class AssetNotFoundException extends RuntimeException {

    public AssetNotFoundException(String message) {
        super(message);
    }
}
