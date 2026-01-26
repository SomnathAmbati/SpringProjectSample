package com.example.SpringProject.seating;

import java.util.List;

import com.example.SpringProject.show.Show;
import com.example.SpringProject.show.ShowDTO;

public interface SeatService {
    List<SeatDTO> getSeatChart(Long showId);
    double calculatePrice(List<Long> seatIds);
    // ShowDTO getShowFromSeat(Long showId);
}
