package com.example.SpringProject.booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringProject.Exception.BadRequestException;
import com.example.SpringProject.Exception.ConflictException;
import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.common.AppEnums.SeatType;
import com.example.SpringProject.seating.SeatEntity;
import com.example.SpringProject.seating.SeatRepository;
import com.example.SpringProject.show.Show;
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
    public Booking createBooking(User user, BookingDTO dto) 
            throws ResourceNotFoundException, BadRequestException, ConflictException {

        if (dto.getSeatIds() == null || dto.getSeatIds().isEmpty()) {
            throw new BadRequestException("Service.NO_SEATS_SELECTED");
        }

        // Verify show exists
        Show show = showRepo.findById(dto.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Service.SHOW_NOT_FOUND"));

        // Fetch selected seats
        List<SeatEntity> seats = seatRepo.findAllById(dto.getSeatIds());

        if (seats.isEmpty()) {
            throw new ResourceNotFoundException("Service.SEATS_NOT_FOUND");
        }

        if (seats.size() != dto.getSeatIds().size()) {
            throw new ResourceNotFoundException("Service.SEATS_NOT_FOUND");
        }

        // Validate all seats are AVAILABLE
        boolean anyBooked = seats.stream()
                .anyMatch(s -> s.getStatus() == AppEnums.SeatStatus.BOOKED);

        if (anyBooked) {
            throw new ConflictException("Service.SEAT_ALREADY_BOOKED");
        }

        // Calculate total price
        double total = seats.stream()
                .mapToDouble(s ->
                        s.getSeatType() == SeatType.PREMIUM ? 250 : 180)
                .sum();

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
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

    @Override
    public Booking getBookingById(Long bookingId) throws ResourceNotFoundException {
        return bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Service.BOOKING_NOT_FOUND"));
    }
}

