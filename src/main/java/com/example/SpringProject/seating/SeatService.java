package com.example.SpringProject.seating;

import java.util.List;

import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.show.Show;
import com.example.SpringProject.show.ShowDTO;

public interface SeatService {
    List<SeatDTO> getSeatChart(Long showId) throws ResourceNotFoundException;
    double calculatePrice(List<Long> seatIds) throws ResourceNotFoundException;
    // ShowDTO getShowFromSeat(Long showId);
    List<SeatDTO> getSeatsByIds(List<Long> seatIds) throws ResourceNotFoundException;
}
