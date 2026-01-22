package com.example.SpringProject.seating;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.theatre.ScreenEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "seat")
public class SeatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber; // A1, A2, B1...

    @Enumerated(EnumType.STRING)
    private AppEnums.SeatType seatType;

    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    private ScreenEntity screen;
}

