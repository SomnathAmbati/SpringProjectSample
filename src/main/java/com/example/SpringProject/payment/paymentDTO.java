package com.example.SpringProject.payment;

import com.example.SpringProject.common.AppEnums;

import lombok.Data;

@Data
public class PaymentDTO {
    private Long bookingId;
    private AppEnums.PaymentMode mode;     // CREDIT / DEBIT
}
