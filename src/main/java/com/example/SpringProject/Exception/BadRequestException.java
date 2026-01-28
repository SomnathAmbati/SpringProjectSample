package com.example.SpringProject.Exception;

public class BadRequestException extends ICinemaException {
    private static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(message);
    }
}
