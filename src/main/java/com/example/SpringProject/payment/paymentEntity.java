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
import lombok.Data;


@Entity
@Table(name = "payments")
@Data
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    private AppEnums.PaymentMode mode;

    private double finalAmount;

    @Enumerated(EnumType.STRING)
    private AppEnums.PaymentStatus status;
}
