package com.authentication.system.security_project.exceptions;

public class AdminDoesnotExistsException extends RuntimeException {
    public AdminDoesnotExistsException(String message) {
        super(message);
    }
}
