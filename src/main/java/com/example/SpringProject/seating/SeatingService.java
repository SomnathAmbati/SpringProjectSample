package com.example.SpringProject.seating;

import java.util.List;

import com.example.SpringProject.common.AppEnums.SeatType;

public interface SeatingService {

    SeatingEntity addSeat(SeatingDTO seat);

    ShowSeatPriceEntity setSeatPrice(Long showId, SeatType seatType, Double price);

    List<SeatStatusMapping> getSeatChart(Long showId);

    void lockSeats(Long showId, List<Long> seatIds);

    double calculateTotalPrice(Long showId, List<Long> seatIds);
    
}
