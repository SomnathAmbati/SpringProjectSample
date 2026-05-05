package com.example.SpringProject.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.example.SpringProject.common.AppEnums;

@Table(name = "movies")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String description;

    private String genre;     // comma-separated genres
    private String language;  // comma-separated languages
    @Column(length = 10000)
    private String imageUrl;         // standard image
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    private AppEnums.CensorRating censorRating;

    private int ratingSum;     // total of all ratings
    private int ratingCount;   // number of users rated
    private double averageRating;

    public void addRating(int rating) {
        this.ratingSum += rating;
        this.ratingCount++;
        this.averageRating = Math.round(ratingSum * 100.0 / ratingCount) / 100.0;
    }
}

