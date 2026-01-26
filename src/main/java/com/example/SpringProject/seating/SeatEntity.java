package com.example.SpringProject.seating;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.show.Show;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "seats")
@Entity
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber; // A1, A2, B1...

    @Enumerated(EnumType.STRING)
    private AppEnums.SeatType seatType; // PREMIUM / REGULAR

    @Enumerated(EnumType.STRING)
    private AppEnums.SeatStatus status; // AVAILABLE / BOOKED

    @ManyToOne(optional = false)
    private Show show;
}
