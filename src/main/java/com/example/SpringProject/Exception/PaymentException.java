package com.example.SpringProject.Exception;

public class PaymentException extends ICinemaException {
    private static final long serialVersionUID = 1L;

    public PaymentException(String message) {
        super(message);
    }
}
