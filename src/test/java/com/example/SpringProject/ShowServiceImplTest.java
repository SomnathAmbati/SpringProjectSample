package com.example.SpringProject;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.SpringProject.Exception.BadRequestException;
import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.common.AppEnums.*;
import com.example.SpringProject.movie.MovieEntity;
import com.example.SpringProject.movie.MovieRepository;
import com.example.SpringProject.seating.SeatRepository;
import com.example.SpringProject.show.Show;
import com.example.SpringProject.show.ShowDTO;
import com.example.SpringProject.show.ShowRepository;
import com.example.SpringProject.show.ShowServiceImpl;
import com.example.SpringProject.theatre.TheatreEntity;
import com.example.SpringProject.theatre.TheatreRepository;

@ExtendWith(MockitoExtension.class)
public class ShowServiceImplTest {

    @Mock
    private ShowRepository showRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private TheatreRepository theatreRepository;

    @Mock
    private SeatRepository seatRepository;
    @InjectMocks
    private ShowServiceImpl showService;

    // ==================== GET SHOWS BY MOVIE TESTS ====================

    @Test
    public void getShowsByMovieTestValid() throws ResourceNotFoundException {
        // Arrange
        MovieEntity movie = new MovieEntity();
        movie.setId(1L);
        movie.setName("Inception");
        movie.setGenre("Sci-Fi");
        movie.setCensorRating(CensorRating.UA);

        TheatreEntity theatre = new TheatreEntity();
        theatre.setId(1L);
        theatre.setName("PVR Cinemas");
        theatre.setLocation("Bangalore");

        Show show1 = new Show();
        show1.setId(1L);
        show1.setMovie(movie);
        show1.setTheatre(theatre);
        show1.setShowTime(LocalDateTime.of(2026, 2, 1, 18, 30));

        Show show2 = new Show();
        show2.setId(2L);
        show2.setMovie(movie);
        show2.setTheatre(theatre);
        show2.setShowTime(LocalDateTime.of(2026, 2, 1, 21, 0));

        Mockito.when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        Mockito.when(showRepository.findByMovieId(1L)).thenReturn(Arrays.asList(show1, show2));

        // Act
        List<ShowDTO> result = showService.getShowsByMovie(1L);

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Inception", result.get(0).getMovie().getName());
        Assertions.assertEquals("PVR Cinemas", result.get(0).getTheatre().getName());
        Mockito.verify(movieRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(showRepository, Mockito.times(1)).findByMovieId(1L);
    }

    @Test
    public void getShowsByMovieTestMovieNotFound() {
        // Arrange
        Mockito.when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> showService.getShowsByMovie(999L)
        );
        Assertions.assertEquals("Service.MOVIE_NOT_FOUND", exception.getMessage());
        Mockito.verify(movieRepository, Mockito.times(1)).findById(999L);
        Mockito.verify(showRepository, Mockito.times(0)).findByMovieId(Mockito.anyLong());
    }

    @Test
    public void getShowsByMovieTestEmptyList() throws ResourceNotFoundException {
        // Arrange
        MovieEntity movie = new MovieEntity();
        movie.setId(1L);

        Mockito.when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        Mockito.when(showRepository.findByMovieId(1L)).thenReturn(Arrays.asList());

        // Act
        List<ShowDTO> result = showService.getShowsByMovie(1L);

        // Assert
        Assertions.assertEquals(0, result.size());
        Mockito.verify(showRepository, Mockito.times(1)).findByMovieId(1L);
    }

    // ==================== GET SHOW BY ID TESTS ====================

    @Test
    public void getShowByIdTestValid() throws ResourceNotFoundException {
        // Arrange
        MovieEntity movie = new MovieEntity();
        movie.setId(1L);
        movie.setName("Inception");

        TheatreEntity theatre = new TheatreEntity();
        theatre.setId(1L);
        theatre.setName("PVR Cinemas");

        Show show = new Show();
        show.setId(1L);
        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowTime(LocalDateTime.of(2026, 2, 1, 18, 30));

        Mockito.when(showRepository.findById(1L)).thenReturn(Optional.of(show));

        // Act
        ShowDTO result = showService.getShowById(1L);

        // Assert
        Assertions.assertEquals("Inception", result.getMovie().getName());
        Assertions.assertEquals("PVR Cinemas", result.getTheatre().getName());
        Mockito.verify(showRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void getShowByIdTestInvalidId() {
        // Arrange
        Mockito.when(showRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> showService.getShowById(999L)
        );
        Assertions.assertEquals("Service.SHOW_NOT_FOUND", exception.getMessage());
        Mockito.verify(showRepository, Mockito.times(1)).findById(999L);
    }

    // ==================== CREATE SHOW TESTS ====================

    @Test
    public void createShowWithIdTestValid() throws ResourceNotFoundException, BadRequestException {
        // Arrange
        MovieEntity movie = new MovieEntity();
        movie.setId(1L);
        movie.setName("Inception");

        TheatreEntity theatre = new TheatreEntity();
        theatre.setId(1L);
        theatre.setName("PVR Cinemas");

        LocalDateTime futureTime = LocalDateTime.now().plusDays(1);

        Show show = new Show();
        show.setId(1L);
        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowTime(futureTime);

        Mockito.when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        Mockito.when(theatreRepository.findById(1L)).thenReturn(Optional.of(theatre));
        Mockito.when(showRepository.save(Mockito.any(Show.class))).thenReturn(show);
        Mockito.when(seatRepository.saveAll(Mockito.anyList())).thenReturn(Arrays.asList());

        // Act
        Show result = showService.createShowWithId(1L, 1L, futureTime);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Inception", result.getMovie().getName());
        Assertions.assertEquals("PVR Cinemas", result.getTheatre().getName());
        Mockito.verify(movieRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(theatreRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(showRepository, Mockito.times(1)).save(Mockito.any(Show.class));
        Mockito.verify(seatRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    @Test
    public void createShowWithIdTestPastTime() {
        // Arrange
        LocalDateTime pastTime = LocalDateTime.now().minusDays(1);

        // Act & Assert
        BadRequestException exception = Assertions.assertThrows(
            BadRequestException.class,
            () -> showService.createShowWithId(1L, 1L, pastTime)
        );
        Assertions.assertEquals("Service.INVALID_SHOW_TIME", exception.getMessage());
        Mockito.verify(movieRepository, Mockito.times(0)).findById(Mockito.anyLong());
    }

    @Test
    public void createShowWithIdTestMovieNotFound() {
        // Arrange
        LocalDateTime futureTime = LocalDateTime.now().plusDays(1);
        Mockito.when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> showService.createShowWithId(999L, 1L, futureTime)
        );
        Assertions.assertEquals("Service.MOVIE_NOT_FOUND", exception.getMessage());
        Mockito.verify(movieRepository, Mockito.times(1)).findById(999L);
    }

    @Test
    public void createShowWithIdTestTheatreNotFound() {
        // Arrange
        MovieEntity movie = new MovieEntity();
        movie.setId(1L);

        LocalDateTime futureTime = LocalDateTime.now().plusDays(1);

        Mockito.when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        Mockito.when(theatreRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> showService.createShowWithId(1L, 999L, futureTime)
        );
        Assertions.assertEquals("Service.THEATRE_NOT_FOUND", exception.getMessage());
        Mockito.verify(theatreRepository, Mockito.times(1)).findById(999L);
    }

    // ==================== DELETE SHOW TESTS ====================

    @Test
    public void deleteShowTestValid() throws ResourceNotFoundException {
        // Arrange
        Show show = new Show();
        show.setId(1L);

        Mockito.when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        Mockito.doNothing().when(showRepository).deleteById(1L);

        // Act
        showService.deleteShow(1L);

        // Assert
        Mockito.verify(showRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(showRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void deleteShowTestInvalidId() {
        // Arrange
        Mockito.when(showRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> showService.deleteShow(999L)
        );
        Assertions.assertEquals("Service.SHOW_NOT_FOUND", exception.getMessage());
        Mockito.verify(showRepository, Mockito.times(1)).findById(999L);
        Mockito.verify(showRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
    }
}
