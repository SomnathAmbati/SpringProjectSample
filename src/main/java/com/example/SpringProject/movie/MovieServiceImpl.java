package com.example.SpringProject.movie;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service(value = "movieService")
@Transactional
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieRepository movieRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .toList();
    }

    @Override
    public MovieDTO getMovie(Long id) {
        MovieEntity movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        return modelMapper.map(movie, MovieDTO.class);
    }

    @Override
    public void addRating(Long movieId, int rating) {
        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.addRating(rating);
        // save() technically optional if @Transactional is present
        // movieRepository.save(movie);
    }


    //ADMIN Methods
    @Override
    public MovieEntity addMovie(MovieDTO dto) {
        return movieRepository.save(modelMapper.map(dto, MovieEntity.class));
    }

    @Override
    public MovieEntity updateMovie(Long id, MovieDTO dto) {
        MovieEntity movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie = modelMapper.map(dto, MovieEntity.class);
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
