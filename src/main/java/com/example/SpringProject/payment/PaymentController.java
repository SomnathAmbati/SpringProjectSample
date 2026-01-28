package com.example.SpringProject.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringProject.Exception.ICinemaException;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/payments")
@Validated
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Process payment for a booking
     * This will:
     *  - Book seats
     *  - Confirm booking
     */
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> makePayment(@Valid @RequestBody PaymentDTO paymentDTO) 
            throws ICinemaException {

        PaymentResponseDTO response = paymentService.processPayment(paymentDTO);
        return ResponseEntity.ok(response);
    }
}