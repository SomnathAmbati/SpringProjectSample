package com.example.SpringProject.seating;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.theatre.ScreenDTO;

import lombok.Data;

@Data
public class SeatingDTO {
    private Long id;

    private String seatNumber; // A1, A2, B1...

    private AppEnums.SeatType seatType;
    private ScreenDTO screen;
}
