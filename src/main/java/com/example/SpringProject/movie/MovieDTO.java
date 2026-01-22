package com.example.SpringProject.movie;

import java.time.LocalDate;
import java.util.Set;

import com.example.SpringProject.common.AppEnums;

import lombok.Data;

@Data
public class MovieDTO {
     private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private String censorRating;
    private Double averageRating;
    private String imageUrl;

    private Set<AppEnums.GenreType> genres;
    private Set<AppEnums.LanguageType> languages;

}
