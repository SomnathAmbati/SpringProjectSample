package com.example.SpringProject.payment;

import com.example.SpringProject.common.AppEnums.PaymentStatus;

import lombok.Data;

@Data
public class PaymentResponseDTO {

    private Long paymentId;
    private PaymentStatus status;
    private double amount;
}
