package com.example.SpringProject.theatre;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.SpringProject.movie.MovieEntity;
import com.example.SpringProject.movie.MovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;
    private final ScreenRepository screenRepository;
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;

    // -------- ADMIN METHODS --------
    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public TheatreEntity createTheatre(TheatreDTO theatre) {
        return theatreRepository.save(modelMapper.map(theatre, TheatreEntity.class));
    }

    @Override
    public ScreenEntity addScreen(Long theatreId, ScreenDTO screen) {
        TheatreEntity theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new RuntimeException("Theatre not found"));

        ScreenEntity se = modelMapper.map(screen, ScreenEntity.class);
        se.setTheatre(theatre);
        return screenRepository.save(se);
    }

    @Override
    public ShowEntity createShow(Long movieId, Long screenId, LocalDateTime showTime) {
        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        ScreenEntity screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        ShowEntity show = new ShowEntity();
        show.setMovie(movie);
        show.setScreen(screen);
        show.setShowTime(showTime);

        return showRepository.save(show);
    }

    // -------- USER METHODS --------

    @Override
    public List<ShowEntity> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieId(movieId);
    }

    @Override
    public ShowEntity getShowById(Long showId) {
        return showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));
    }
}
