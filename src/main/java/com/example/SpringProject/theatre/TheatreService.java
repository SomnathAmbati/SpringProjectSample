package com.example.SpringProject.theatre;

import java.util.List;

public interface TheatreService {

    TheatreDTO createTheatre(TheatreDTO theatreDTO);

    List<TheatreDTO> getAllTheatres();

    TheatreDTO getTheatreById(Long id);

    TheatreEntity create(TheatreDTO dto);

    TheatreEntity update(Long id, TheatreDTO dto);

    void delete(Long id);
}
