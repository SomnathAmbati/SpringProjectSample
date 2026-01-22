package com.example.SpringProject.movie;

import java.time.LocalDate;
import java.util.Set;

import com.example.SpringProject.common.AppEnums;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "movie")
@Entity
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDate releaseDate;
    private String censorRating;
    private Double averageRating;
    private String imageUrl;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<AppEnums.GenreType> genres;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<AppEnums.LanguageType> languages;
}
