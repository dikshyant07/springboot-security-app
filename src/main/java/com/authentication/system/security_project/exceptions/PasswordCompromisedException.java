package com.authentication.system.security_project.exceptions;

public class PasswordCompromisedException extends RuntimeException {
    public PasswordCompromisedException(String message) {
        super(message);
    }
}
