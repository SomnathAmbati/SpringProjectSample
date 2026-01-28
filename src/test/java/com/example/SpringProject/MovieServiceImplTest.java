package com.example.SpringProject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.SpringProject.Exception.BadRequestException;
import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.movie.MovieDTO;
import com.example.SpringProject.movie.MovieEntity;
import com.example.SpringProject.movie.MovieRepository;
import com.example.SpringProject.movie.MovieServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    // ==================== GET ALL MOVIES ====================

    @Test
    void getAllMovies_valid() {
        MovieEntity movie1 = new MovieEntity();
        movie1.setId(1L);
        movie1.setName("Inception");

        MovieEntity movie2 = new MovieEntity();
        movie2.setId(2L);
        movie2.setName("Avatar");

        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie1, movie2));

        List<MovieDTO> result = movieService.getAllMovies();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Inception", result.get(0).getName());
        Assertions.assertEquals("Avatar", result.get(1).getName());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void getAllMovies_empty() {
        when(movieRepository.findAll()).thenReturn(List.of());

        List<MovieDTO> result = movieService.getAllMovies();

        Assertions.assertTrue(result.isEmpty());
        verify(movieRepository, times(1)).findAll();
    }

    // ==================== GET MOVIE BY ID ====================

    @Test
    void getMovie_validId() throws ResourceNotFoundException {
        MovieEntity movie = new MovieEntity();
        movie.setId(1L);
        movie.setName("Inception");
        movie.setAverageRating(4.5);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        MovieDTO dto = movieService.getMovie(1L);

        Assertions.assertEquals("Inception", dto.getName());
        Assertions.assertEquals(4.5, dto.getAverageRating());
        verify(movieRepository).findById(1L);
    }

    @Test
    void getMovie_invalidId() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex =
                Assertions.assertThrows(ResourceNotFoundException.class,
                        () -> movieService.getMovie(999L));

        Assertions.assertEquals("Service.MOVIE_NOT_FOUND", ex.getMessage());
        verify(movieRepository).findById(999L);
    }

    // ==================== ADD RATING ====================

    @Test
    void addRating_valid() throws Exception {
        MovieEntity movie = new MovieEntity();
        movie.setId(1L);
        movie.setRatingSum(20);
        movie.setRatingCount(5);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(MovieEntity.class))).thenReturn(movie);

        movieService.addRating(1L, 5);

        Assertions.assertEquals(25, movie.getRatingSum());
        Assertions.assertEquals(6, movie.getRatingCount());
        verify(movieRepository).save(movie);
    }

    @Test
    void addRating_invalid_low() {
        BadRequestException ex =
                Assertions.assertThrows(BadRequestException.class,
                        () -> movieService.addRating(1L, 0));

        Assertions.assertEquals("Service.INVALID_RATING", ex.getMessage());
        verify(movieRepository, never()).findById(anyLong());
    }

    @Test
    void addRating_movieNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex =
                Assertions.assertThrows(ResourceNotFoundException.class,
                        () -> movieService.addRating(1L, 4));

        Assertions.assertEquals("Service.MOVIE_NOT_FOUND", ex.getMessage());
    }

    // ==================== ADD MOVIE ====================

    @Test
    void addMovie_valid() {
        MovieDTO dto = new MovieDTO();
        dto.setName("Inception");
        dto.setGenre("Sci-Fi");
        dto.setReleaseDate(LocalDate.now());

        MovieEntity saved = new MovieEntity();
        saved.setId(1L);
        saved.setName("Inception");

        when(movieRepository.save(any(MovieEntity.class))).thenReturn(saved);

        MovieEntity result = movieService.addMovie(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Inception", result.getName());
        verify(movieRepository).save(any(MovieEntity.class));
    }

    // ==================== UPDATE MOVIE ====================

    @Test
    void updateMovie_valid() throws Exception {
        MovieEntity movie = new MovieEntity();
        movie.setId(1L);
        movie.setName("Old");

        MovieDTO dto = new MovieDTO();
        dto.setName("New");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);

        MovieEntity result = movieService.updateMovie(1L, dto);

        Assertions.assertEquals("New", result.getName());
        verify(movieRepository).save(movie);
    }

    @Test
    void updateMovie_invalidId() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> movieService.updateMovie(1L, new MovieDTO()));
    }

    // ==================== DELETE MOVIE ====================

    @Test
    void deleteMovie_valid() throws Exception {
        MovieEntity movie = new MovieEntity();
        movie.setId(1L);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        doNothing().when(movieRepository).deleteById(1L);

        movieService.deleteMovie(1L);

        verify(movieRepository).deleteById(1L);
    }

    @Test
    void deleteMovie_invalid() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> movieService.deleteMovie(1L));

        verify(movieRepository, never()).deleteById(anyLong());
    }
}
