package com.authentication.system.security_project.exceptions;

public class TokenDoesNotExistsException extends RuntimeException {
    public TokenDoesNotExistsException(String message) {
        super(message);
    }
}
