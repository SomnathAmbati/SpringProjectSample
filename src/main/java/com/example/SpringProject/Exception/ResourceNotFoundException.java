package com.example.SpringProject.Exception;

public class ResourceNotFoundException extends ICinemaException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}