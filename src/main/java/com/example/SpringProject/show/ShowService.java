package com.example.SpringProject.show;

import java.time.LocalDateTime;
import java.util.List;

import com.example.SpringProject.Exception.BadRequestException;
import com.example.SpringProject.Exception.ResourceNotFoundException;

public interface ShowService {
    List<ShowDTO> getShowsByMovie(Long movieId) throws ResourceNotFoundException;

    // Show createShow(Long movieId, Long theatreId, LocalDateTime time);

    void deleteShow(Long showId) throws ResourceNotFoundException;

    ShowDTO getShowById(Long showId) throws ResourceNotFoundException;

    Show createShowWithId(Long movieId, Long theatreId, LocalDateTime time) throws ResourceNotFoundException, BadRequestException;

}

