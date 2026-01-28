package com.example.SpringProject.seating;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.show.Show;
import com.example.SpringProject.show.ShowRepository;

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
    public List<SeatDTO> getSeatChart(Long showId) throws ResourceNotFoundException {
        // Verify show exists
        Optional<Show> showOpt = showRepo.findById(showId);
        if (!showOpt.isPresent()) {
            throw new ResourceNotFoundException("Service.SHOW_NOT_FOUND");
        }

        return seatRepo.findByShowId(showId)
                .stream()
                .map(seat -> modelMapper.map(seat, SeatDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public double calculatePrice(List<Long> seatIds) throws ResourceNotFoundException {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new ResourceNotFoundException("Service.NO_SEATS_SELECTED");
        }

        return seatRepo.findAllById(seatIds)
                .stream()
                .mapToDouble(seat ->
                        seat.getSeatType() == AppEnums.SeatType.PREMIUM ? 250 : 180)
                .sum();
    }

    @Override
    public List<SeatDTO> getSeatsByIds(List<Long> seatIds) throws ResourceNotFoundException {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new ResourceNotFoundException("Service.NO_SEATS_SELECTED");
        }

        List<SeatEntity> seats = seatRepo.findByIdIn(seatIds);

        if (seats.isEmpty()) {
            throw new ResourceNotFoundException("Service.SEATS_NOT_FOUND");
        }

        return seats.stream()
                .map(seat -> modelMapper.map(seat, SeatDTO.class))
                .collect(Collectors.toList());
    }
}