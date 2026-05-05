
package com.example.SpringProject.payment;

import com.example.SpringProject.booking.Booking;
import com.example.SpringProject.common.AppEnums;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name = "payments")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    private AppEnums.PaymentMode mode; // CREDIT / DEBIT

    private double finalAmount;

    @Enumerated(EnumType.STRING)
    private AppEnums.PaymentStatus status;
}
