package com.example.SpringProject.booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.common.AppEnums.BookingStatus;
import com.example.SpringProject.common.AppEnums.SeatType;
import com.example.SpringProject.seating.SeatEntity;
import com.example.SpringProject.seating.SeatRepository;
import com.example.SpringProject.show.ShowRepository;
import com.example.SpringProject.user.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private SeatRepository seatRepo;

    @Autowired
    private ShowRepository showRepo;

    @Override
    public Booking createBooking(User user, BookingDTO dto) {

        List<SeatEntity> seats = seatRepo.findAllById(dto.getSeatIds());

        if (seats.isEmpty()) {
            throw new RuntimeException("No seats selected");
        }

        // Validate all seats are AVAILABLE
        boolean anyBooked = seats.stream()
                .anyMatch(s -> s.getStatus() == AppEnums.SeatStatus.BOOKED);

        if (anyBooked) {
            throw new RuntimeException("One or more seats already booked");
        }

        double total = seats.stream()
                .mapToDouble(s ->
                        s.getSeatType() == SeatType.PREMIUM ? 250 : 180)
                .sum();

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(showRepo.findById(dto.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found")));

        booking.setSeatNumbers(
                seats.stream()
                        .map(SeatEntity::getSeatNumber)
                        .collect(Collectors.joining(","))
        );

        booking.setTotalPrice(total);
        booking.setStatus(AppEnums.BookingStatus.INITIATED);
        booking.setBookingTime(LocalDateTime.now());

        return bookingRepo.save(booking);
    }
}
