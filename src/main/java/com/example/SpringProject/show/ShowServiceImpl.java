package com.example.SpringProject.show;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringProject.movie.MovieRepository;
import com.example.SpringProject.theatre.TheatreDTO;
import com.example.SpringProject.theatre.TheatreRepository;


@Service
public class ShowServiceImpl implements ShowService {
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private TheatreRepository theatreRepository;

    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public List<ShowDTO> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieId(movieId)
                .stream()
                .map(show -> {
                    ShowDTO dto = new ShowDTO();
                    dto.setId(show.getId());
                    dto.setMovieId(show.getMovie().getId());
                    dto.setTheatre(modelMapper.map(show.getTheatre(), TheatreDTO.class));
                    dto.setShowTime(show.getShowTime());
                    return dto;
                }).toList();
    }

    @Override
    public Show createShow(Long movieId, Long theatreId, LocalDateTime time) {
        Show show = new Show();
        show.setMovie(movieRepository.findById(movieId).orElseThrow());
        show.setTheatre(theatreRepository.findById(theatreId).orElseThrow());
        show.setShowTime(time);
        return showRepository.save(show);
    }

    @Override
    public void deleteShow(Long showId) {
        showRepository.deleteById(showId);
    }
}

