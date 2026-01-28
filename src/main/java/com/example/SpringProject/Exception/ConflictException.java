package com.example.SpringProject.Exception;

public class ConflictException extends ICinemaException {
    private static final long serialVersionUID = 1L;

    public ConflictException(String message) {
        super(message);
    }
}
