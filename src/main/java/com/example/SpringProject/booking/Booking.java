package com.example.SpringProject.booking;

import java.time.LocalDateTime;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.show.Show;
import com.example.SpringProject.user.User;

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
    private User user;

    @ManyToOne(optional = false)
    private Show show;

    private String seatNumbers; // "A1,A2,B3"

    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private AppEnums.BookingStatus status;

    private LocalDateTime bookingTime;
}
