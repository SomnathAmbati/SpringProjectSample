package com.example.SpringProject.theatre;

import java.time.LocalDateTime;
import java.util.List;

public interface TheatreService {

    TheatreEntity createTheatre(TheatreDTO theatre);

    ScreenEntity addScreen(Long theatreId, ScreenDTO screen);

    ShowEntity createShow(Long movieId, Long screenId, LocalDateTime showTime);

    List<ShowEntity> getShowsByMovie(Long movieId);

    ShowEntity getShowById(Long showId);

}
