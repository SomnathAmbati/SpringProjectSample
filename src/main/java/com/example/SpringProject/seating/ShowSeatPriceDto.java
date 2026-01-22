package com.example.SpringProject.seating;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.theatre.ShowEntity;

import lombok.Data;

@Data
public class ShowSeatPriceDto {
 private Long id;

    private ShowEntity show;
    private AppEnums.SeatType seatType;
    private Double price;
}
