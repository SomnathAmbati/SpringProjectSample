package com.example.SpringProject.theatre;

import java.time.LocalDateTime;

import com.example.SpringProject.movie.MovieDTO;

public class ShowDTO {
    private Long id;
    private String screenName;

    private MovieDTO movie;
    private ScreenDTO screen;
    private LocalDateTime showTime;

}
