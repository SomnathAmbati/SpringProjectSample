package com.example.SpringProject.show;

import java.time.LocalDateTime;

import com.example.SpringProject.movie.MovieDTO;
import com.example.SpringProject.theatre.TheatreDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowDTO {
    private Long id;
    private MovieDTO movie;
    private TheatreDTO theatre;
    private LocalDateTime showTime;
}

