package com.example.SpringProject.payment;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.common.AppEnums.PaymentStatus;

import lombok.Data;

@Data
public class PaymentResponseDTO {

    private Long paymentId;
    private AppEnums.PaymentStatus status;
    private double amount;
}