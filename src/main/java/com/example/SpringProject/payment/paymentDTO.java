package com.example.SpringProject.payment;

import com.example.SpringProject.common.AppEnums;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentDTO {
    @NotNull(message = "{payment.bookingId.invalid}")
    private Long bookingId;
    
    @NotBlank(message = "{payment.mode.invalid}")
    private AppEnums.PaymentMode mode;  
}
