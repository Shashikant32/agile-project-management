package com.agilepm.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long resourceId) {
        super(String.format("%s not found with id: %d", resourceName, resourceId));
    }

    public ResourceNotFoundException(String resourceName, String identifier, String value) {
        super(String.format("%s not found with %s: %s", resourceName, identifier, value));
    }
}
