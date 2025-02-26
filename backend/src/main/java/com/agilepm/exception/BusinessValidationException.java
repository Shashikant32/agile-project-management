package com.agilepm.exception;

public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) {
        super(message);
    }

    public static BusinessValidationException duplicateResource(String resourceName, String identifier) {
        return new BusinessValidationException(
            String.format("A %s with this %s already exists", resourceName, identifier)
        );
    }

    public static BusinessValidationException invalidState(String resourceName, String reason) {
        return new BusinessValidationException(
            String.format("Invalid state for %s: %s", resourceName, reason)
        );
    }
}
