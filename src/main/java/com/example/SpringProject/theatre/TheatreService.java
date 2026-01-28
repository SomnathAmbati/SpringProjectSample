package com.example.SpringProject.theatre;

import java.util.List;

import com.example.SpringProject.Exception.ResourceNotFoundException;

public interface TheatreService {

    TheatreDTO createTheatre(TheatreDTO theatreDTO);

    List<TheatreDTO> getAllTheatres();

    TheatreDTO getTheatreById(Long id) throws ResourceNotFoundException;

    TheatreEntity create(TheatreDTO dto);

    TheatreEntity update(Long id, TheatreDTO dto) throws ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;
}
