package com.example.SpringProject.movie;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringProject.Exception.BadRequestException;
import com.example.SpringProject.Exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service(value = "movieService")
@Transactional
public class MovieServiceImpl implements MovieService {
    
    @Autowired
    private MovieRepository movieRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public MovieDTO getMovie(Long id) throws ResourceNotFoundException {
        MovieEntity movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service.MOVIE_NOT_FOUND"));

        return modelMapper.map(movie, MovieDTO.class);
    }

    @Override
    public void addRating(Long movieId, int rating) throws ResourceNotFoundException, BadRequestException {
        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Service.INVALID_RATING");
        }

        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Service.MOVIE_NOT_FOUND"));

        movie.addRating(rating);
        movieRepository.save(movie);
    }

    // ADMIN Methods
    @Override
    public MovieEntity addMovie(MovieDTO dto) {
        return movieRepository.save(modelMapper.map(dto, MovieEntity.class));
    }

    @Override
    public MovieEntity updateMovie(Long id, MovieDTO dto) throws ResourceNotFoundException {
        MovieEntity movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service.MOVIE_NOT_FOUND"));
        
        // Update fields
        movie.setName(dto.getName());
        movie.setDescription(dto.getDescription());
        movie.setGenre(dto.getGenre());
        movie.setLanguage(dto.getLanguage());
        movie.setImageUrl(dto.getImageUrl());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setCensorRating(dto.getCensorRating());
        
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Long id) throws ResourceNotFoundException {
        Optional<MovieEntity> movieOpt = movieRepository.findById(id);
        if (!movieOpt.isPresent()) {
            throw new ResourceNotFoundException("Service.MOVIE_NOT_FOUND");
        }
        movieRepository.deleteById(id);
    }
}
