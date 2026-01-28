package com.example.SpringProject.seating;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.show.Show;
import com.example.SpringProject.show.ShowDTO;
import com.example.SpringProject.show.ShowRepository;
import com.example.SpringProject.theatre.TheatreDTO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SeatServiceImpl implements SeatService {
    @Autowired
    private SeatRepository seatRepo;
    @Autowired
    private ShowRepository showRepo;

    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public List<SeatDTO> getSeatChart(Long showId) {
        return seatRepo.findByShowId(showId)
                .stream()
                .map(seat -> modelMapper.map(seat, SeatDTO.class))
                .toList();
    }
    
    // @Override
    // public ShowDTO getShowFromSeat(Long showId){
    //     Show s = showRepo.findById(showId).orElseThrow(() -> new RuntimeException("Show not found"));
    //     TheatreDTO theatreDTO = modelMapper.map(s.getTheatre(), TheatreDTO.class);
    //     ShowDTO showDTO = modelMapper.map(s, ShowDTO.class);
    //     showDTO.setTheatre(theatreDTO);
    //     return showDTO;
    // }

    @Override
    public double calculatePrice(List<Long> seatIds) {
        return seatRepo.findAllById(seatIds)
                .stream()
                .mapToDouble(seat ->
                        seat.getSeatType() == AppEnums.SeatType.PREMIUM ? 250 : 180)
                .sum();
    }
    

    @Override
    public List<SeatDTO> getSeatsByIds(List<Long> seatIds) {

        List<SeatEntity> seats = seatRepo.findByIdIn(seatIds);

        if (seats.isEmpty()) {
            throw new RuntimeException("Seats not found");
        }

        return seats.stream()
                .map(seat -> modelMapper.map(seat, SeatDTO.class))
                .toList();
    }
}
