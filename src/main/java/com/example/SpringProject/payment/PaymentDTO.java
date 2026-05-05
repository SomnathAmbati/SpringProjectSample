

package com.example.SpringProject.payment;

import com.example.SpringProject.common.AppEnums;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    @NotNull(message = "{payment.bookingId.invalid}")
    private Long bookingId;
    
//    @NotBlank(message = "{payment.mode.invalid}")
    private AppEnums.PaymentMode mode;  
}


