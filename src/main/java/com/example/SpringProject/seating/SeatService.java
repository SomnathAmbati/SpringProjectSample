package com.example.SpringProject.seating;

import java.util.List;

import com.example.SpringProject.Exception.ResourceNotFoundException;


public interface SeatService {
    List<SeatDTO> getSeatChart(Long showId) throws ResourceNotFoundException;
    double calculatePrice(List<Long> seatIds) throws ResourceNotFoundException;
    List<SeatDTO> getSeatsByIds(List<Long> seatIds) throws ResourceNotFoundException;
}
