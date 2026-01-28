package com.example.SpringProject.movie;

import java.util.List;

import com.example.SpringProject.Exception.BadRequestException;
import com.example.SpringProject.Exception.ResourceNotFoundException;

public interface MovieService {

    List<MovieDTO> getAllMovies();

    MovieDTO getMovie(Long id) throws ResourceNotFoundException;

    void addRating(Long movieId, int rating) throws ResourceNotFoundException, BadRequestException;

    MovieEntity addMovie(MovieDTO dto);

    MovieEntity updateMovie(Long id, MovieDTO dto) throws ResourceNotFoundException;

    void deleteMovie(Long id) throws ResourceNotFoundException;

    
}