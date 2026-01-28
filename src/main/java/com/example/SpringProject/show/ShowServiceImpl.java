package com.example.SpringProject.show;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringProject.Exception.BadRequestException;
import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.common.AppEnums.SeatType;
import com.example.SpringProject.movie.MovieDTO;
import com.example.SpringProject.movie.MovieEntity;
import com.example.SpringProject.movie.MovieRepository;
import com.example.SpringProject.seating.SeatEntity;
import com.example.SpringProject.seating.SeatRepository;
import com.example.SpringProject.theatre.TheatreDTO;
import com.example.SpringProject.theatre.TheatreEntity;
import com.example.SpringProject.theatre.TheatreRepository;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShowServiceImpl implements ShowService {
    
    @Autowired
    private ShowRepository showRepository;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private TheatreRepository theatreRepository;
    
    @Autowired
    private SeatRepository seatRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<ShowDTO> getShowsByMovie(Long movieId) throws ResourceNotFoundException {
        // Verify movie exists
        Optional<MovieEntity> movieOpt = movieRepository.findById(movieId);
        if (!movieOpt.isPresent()) {
            throw new ResourceNotFoundException("Service.MOVIE_NOT_FOUND");
        }

        return showRepository.findByMovieId(movieId)
                .stream()
                .map(show -> {
                    ShowDTO dto = new ShowDTO();
                    dto.setId(show.getId());
                    dto.setMovie(modelMapper.map(show.getMovie(), MovieDTO.class));
                    dto.setTheatre(modelMapper.map(show.getTheatre(), TheatreDTO.class));
                    dto.setShowTime(show.getShowTime());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public ShowDTO getShowById(Long showId) throws ResourceNotFoundException {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Service.SHOW_NOT_FOUND"));
        
        ShowDTO dto = new ShowDTO();
        dto.setId(show.getId());
        dto.setMovie(modelMapper.map(show.getMovie(), MovieDTO.class));
        dto.setTheatre(modelMapper.map(show.getTheatre(), TheatreDTO.class));
        dto.setShowTime(show.getShowTime());

        return dto;
    }

    @Override
    public Show createShowWithId(Long movieId, Long theatreId, LocalDateTime time) 
            throws ResourceNotFoundException, BadRequestException {
        
        // Validate show time is in future
        if (time.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Service.INVALID_SHOW_TIME");
        }

        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Service.MOVIE_NOT_FOUND"));
        
        TheatreEntity theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Service.THEATRE_NOT_FOUND"));

        Show show = new Show();
        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowTime(time);
        
        Show savedShow = showRepository.save(show);
        generateSeatsForShow(savedShow);

        return savedShow;
    }

    private void generateSeatsForShow(Show show) {
        List<SeatEntity> seats = new ArrayList<>();

        final int TOTAL_ROWS = 10;     // A–J
        final int TOTAL_COLS = 12;     // 1–12
        final int PREMIUM_ROWS = 2;    // A, B

        for (int row = 0; row < TOTAL_ROWS; row++) {
            char rowChar = (char) ('A' + row);
            SeatType seatType = row < PREMIUM_ROWS ? SeatType.PREMIUM : SeatType.REGULAR;

            for (int col = 1; col <= TOTAL_COLS; col++) {
                SeatEntity seat = new SeatEntity();
                seat.setShow(show);
                seat.setSeatNumber(rowChar + String.valueOf(col));
                seat.setSeatType(seatType);
                seat.setStatus(AppEnums.SeatStatus.AVAILABLE);

                seats.add(seat);
            }
        }

        seatRepository.saveAll(seats);
    }

    @Override
    public void deleteShow(Long showId) throws ResourceNotFoundException {
        Optional<Show> showOpt = showRepository.findById(showId);
        if (!showOpt.isPresent()) {
            throw new ResourceNotFoundException("Service.SHOW_NOT_FOUND");
        }
        showRepository.deleteById(showId);
    }
}







