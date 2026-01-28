package com.example.SpringProject.booking;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingDTO {
    @NotNull(message = "{booking.showId.invalid}")
    private Long showId;
    
    @NotEmpty(message = "{booking.seatIds.invalid}")
    private List<Long> seatIds;
}
