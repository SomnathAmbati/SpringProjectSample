package com.example.SpringProject.movie;
import java.time.LocalDate;

import com.example.SpringProject.common.AppEnums;

import lombok.Data;

@Data
public class MovieDTO {

    private Long id;
    private String name;
    private String description;
    private String genre;
    private String language;
    private String imageUrl;
    private LocalDate releaseDate;
    private AppEnums.CensorRating censorRating;
    private double averageRating;
}
