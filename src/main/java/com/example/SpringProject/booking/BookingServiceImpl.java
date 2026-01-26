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
import com.example.SpringProject.user.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepo;
    @Autowired
    private SeatRepository seatRepo;

    @Override
    public Booking createBooking(User user, BookingDTO dto) {

        List<SeatEntity> seats = seatRepo.findAllById(dto.getSeatIds());

        double total = seats.stream()
                .mapToDouble(s -> s.getSeatType() == SeatType.PREMIUM ? 250 : 180)
                .sum();

        seats.forEach(s -> s.setStatus(AppEnums.SeatStatus.BOOKED));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(seats.get(0).getShow());
        booking.setSeatNumbers(
                seats.stream().map(SeatEntity::getSeatNumber)
                        .collect(Collectors.joining(",")));
        booking.setTotalPrice(total);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setBookingTime(LocalDateTime.now());

        return bookingRepo.save(booking);
    }
}
