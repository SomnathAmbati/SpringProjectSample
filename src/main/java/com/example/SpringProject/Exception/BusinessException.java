package com.example.SpringProject.Exception;

public class BusinessException extends ICinemaException {
    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }
}
