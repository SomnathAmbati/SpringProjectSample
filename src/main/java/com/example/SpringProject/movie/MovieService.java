package com.example.SpringProject.movie;

import java.util.List;

public interface MovieService {

    List<MovieDTO> getAllMovies();

    MovieDTO getMovie(Long id);

    void addRating(Long movieId, int rating);

    MovieEntity addMovie(MovieDTO dto);

    MovieEntity updateMovie(Long id, MovieDTO dto);

    void deleteMovie(Long id);

    
}