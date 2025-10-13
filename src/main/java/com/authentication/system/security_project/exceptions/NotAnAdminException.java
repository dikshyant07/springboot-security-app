package com.authentication.system.security_project.exceptions;

public class NotAnAdminException extends RuntimeException {
    public NotAnAdminException(String message) {
        super(message);
    }
}
