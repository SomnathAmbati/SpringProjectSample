package com.example.SpringProject.movie;
import java.time.LocalDate;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.common.AppEnums.CensorRating;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MovieDTO {
    private Long id;
    
    @NotBlank(message = "{movie.name.invalid}")
    @Size(min = 1, max = 100, message = "{movie.name.invalid}")
    private String name;
    
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;
    
    @NotBlank(message = "{movie.genre.invalid}")
    private String genre;
    
    @NotBlank(message = "{movie.language.invalid}")
    private String language;
    
    private String imageUrl;
    
    @NotNull(message = "{movie.releaseDate.invalid}")
    @PastOrPresent(message = "{movie.releaseDate.invalid}")
    private LocalDate releaseDate;
    
    @NotNull(message = "{movie.censorRating.invalid}")
    private CensorRating censorRating;
    
    private Double averageRating;
}
