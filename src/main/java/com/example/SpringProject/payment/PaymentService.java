package com.example.SpringProject.payment;

import com.example.SpringProject.Exception.BusinessException;
import com.example.SpringProject.Exception.ConflictException;
import com.example.SpringProject.Exception.PaymentException;
import com.example.SpringProject.Exception.ResourceNotFoundException;

public interface PaymentService {

    PaymentResponseDTO processPayment(PaymentDTO paymentDTO) throws ResourceNotFoundException, BusinessException, ConflictException, PaymentException;
}


