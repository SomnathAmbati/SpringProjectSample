package com.example.SpringProject.show;

import java.time.LocalDateTime;

import com.example.SpringProject.theatre.TheatreDTO;

import lombok.Data;

@Data
public class ShowDTO {
    private Long id;
    private Long movieId;
    private TheatreDTO theatre;
    private LocalDateTime showTime;
    // private int availableSeats;
}
