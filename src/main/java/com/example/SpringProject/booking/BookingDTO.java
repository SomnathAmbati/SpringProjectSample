package com.example.SpringProject.booking;

import java.util.List;

import lombok.Data;

@Data
public class BookingDTO {
    private Long showId;
    private List<Long> seatIds;
}
