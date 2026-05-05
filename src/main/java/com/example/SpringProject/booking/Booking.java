package com.example.SpringProject.booking;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private com.example.SpringProject.user.User user = null;

    @ManyToOne(optional = false)
    private com.example.SpringProject.show.Show show;

    private String seatNumbers; // "A1,A2,B3"

    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private com.example.SpringProject.common.AppEnums.BookingStatus status;

    private LocalDateTime bookingTime;
}
