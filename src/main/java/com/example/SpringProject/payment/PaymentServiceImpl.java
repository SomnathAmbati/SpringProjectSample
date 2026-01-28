package com.example.SpringProject.payment;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringProject.Exception.BusinessException;
import com.example.SpringProject.Exception.ConflictException;
import com.example.SpringProject.Exception.PaymentException;
import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.booking.Booking;
import com.example.SpringProject.booking.BookingRepository;
import com.example.SpringProject.common.AppEnums.BookingStatus;
import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.common.AppEnums.PaymentStatus;
import com.example.SpringProject.seating.SeatEntity;
import com.example.SpringProject.seating.SeatRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public PaymentResponseDTO processPayment(PaymentDTO dto) 
            throws ResourceNotFoundException, BusinessException, ConflictException, PaymentException {

        // 1️⃣ Fetch booking
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Service.BOOKING_NOT_FOUND"));

        if (booking.getStatus() != BookingStatus.INITIATED) {
            throw new BusinessException("Service.INVALID_BOOKING_STATE");
        }

        // 2️⃣ Fetch seats
        List<String> seatNumbers = Arrays.asList(booking.getSeatNumbers().split(","));
        List<SeatEntity> seats = seatRepository.findByShowIdAndSeatNumberIn(
                booking.getShow().getId(), seatNumbers);

        if (seats.isEmpty()) {
            throw new ResourceNotFoundException("Service.SEATS_NOT_FOUND");
        }

        // 3️⃣ Validate again (race condition protection)
        for (SeatEntity seat : seats) {
            if (seat.getStatus() == AppEnums.SeatStatus.BOOKED) {
                throw new ConflictException("Service.SEAT_ALREADY_BOOKED");
            }
        }

        // 4️⃣ Mark seats as BOOKED
        seats.forEach(seat -> seat.setStatus(AppEnums.SeatStatus.BOOKED));
        seatRepository.saveAll(seats);

        // 5️⃣ Save payment
        PaymentEntity payment = new PaymentEntity();
        payment.setBooking(booking);
        payment.setMode(dto.getMode());
        payment.setFinalAmount(booking.getTotalPrice());
        payment.setStatus(PaymentStatus.SUCCESS);

        PaymentEntity savedPayment;
        try {
            savedPayment = paymentRepository.save(payment);
        } catch (Exception e) {
            // Rollback seat bookings if payment save fails
            seats.forEach(seat -> seat.setStatus(AppEnums.SeatStatus.AVAILABLE));
            seatRepository.saveAll(seats);
            throw new PaymentException("Service.PAYMENT_FAILED");
        }

        // 6️⃣ Confirm booking
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        // 7️⃣ Response
        PaymentResponseDTO response = modelMapper.map(savedPayment, PaymentResponseDTO.class);
        response.setAmount(savedPayment.getFinalAmount());

        return response;
    }
}