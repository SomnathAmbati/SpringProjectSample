package com.example.SpringProject.movie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringProject.Exception.ICinemaException;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/movies")
@Validated
public class MovieController {
    
    @Autowired
    private MovieService movieService;
    
    @Autowired
    private Environment environment;

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() throws ICinemaException {
        List<MovieDTO> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovie(@PathVariable Long id) throws ICinemaException {
        MovieDTO movie = movieService.getMovie(id);
        return ResponseEntity.ok(movie);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<String> rateMovie(
            @PathVariable Long id,
            @RequestParam 
            @Min(value = 1, message = "{movie.rating.min}")
            @Max(value = 5, message = "{movie.rating.max}")
            int rating) throws ICinemaException {
        
        movieService.addRating(id, rating);
        String message = environment.getProperty("API.RATING_ADDED");
        return ResponseEntity.ok(message);
    }
}
