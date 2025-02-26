package com.agilepm.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public static UnauthorizedException insufficientPermissions(String action) {
        return new UnauthorizedException(
            String.format("Insufficient permissions to perform action: %s", action)
        );
    }

    public static UnauthorizedException authenticationRequired() {
        return new UnauthorizedException("Authentication is required to access this resource");
    }
}
