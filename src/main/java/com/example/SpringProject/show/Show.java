package com.example.SpringProject.show;

import java.time.LocalDateTime;

import com.example.SpringProject.movie.MovieEntity;
import com.example.SpringProject.theatre.TheatreEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "shows")
@Entity
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private MovieEntity movie;

    @ManyToOne(optional = false)
    private TheatreEntity theatre;

    private LocalDateTime showTime;
    // private int availableSeats;
}
