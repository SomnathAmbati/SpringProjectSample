package com.example.SpringProject.show;

import java.time.LocalDateTime;

import com.example.SpringProject.movie.MovieDTO;
import com.example.SpringProject.theatre.TheatreDTO;

import lombok.Data;

@Data
public class ShowDTO {
    private Long id;
    private MovieDTO movie;
    private TheatreDTO theatre;
    private LocalDateTime showTime;
    // private int availableSeats;
}
