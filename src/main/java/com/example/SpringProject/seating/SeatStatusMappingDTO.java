package com.example.SpringProject.seating;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.theatre.ShowDTO;

import lombok.Data;

@Data
public class SeatStatusMappingDTO {
    private Long id;

    private ShowDTO show;

    private SeatingEntity seat;

    private AppEnums.SeatStatus status;
}
