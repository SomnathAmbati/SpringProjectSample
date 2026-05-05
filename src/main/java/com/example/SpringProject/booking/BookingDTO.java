package com.example.SpringProject.booking;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    @NotNull(message = "{booking.showId.invalid}")
    private Long showId;
    
    @NotEmpty(message = "{booking.seatIds.invalid}")
    private List<Long> seatIds;
}

