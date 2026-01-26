package com.example.SpringProject.show;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowService {
    List<ShowDTO> getShowsByMovie(Long movieId);

    Show createShow(Long movieId, Long theatreId, LocalDateTime time);

    void deleteShow(Long showId);
}

