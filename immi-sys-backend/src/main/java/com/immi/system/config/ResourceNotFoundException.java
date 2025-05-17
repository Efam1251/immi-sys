package com.immi.system.config;

public class ResourceNotFoundException extends RuntimeException {

    // Constructor that accepts only a message
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Constructor that accepts both message and cause
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}