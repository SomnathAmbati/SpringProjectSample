package com.example.SpringProject.seating;

import com.example.SpringProject.common.AppEnums;

import lombok.Data;

@Data
public class SeatDTO {
    private Long id;
    private String seatNumber;
    private AppEnums.SeatType seatType;
    private AppEnums.SeatStatus status;
    private Long showId;
}
