package com.example.SpringProject.payment;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringProject.booking.Booking;
import com.example.SpringProject.booking.BookingRepository;
import com.example.SpringProject.common.AppEnums.BookingStatus;
import com.example.SpringProject.common.AppEnums.PaymentStatus;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public PaymentResponseDTO processPayment(PaymentDTO dto) {
        // 1️⃣ Fetch booking
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // 2️⃣ Create Payment entity
        PaymentEntity payment = new PaymentEntity();
        payment.setBooking(booking);
        payment.setFinalAmount(booking.getTotalPrice());
        payment.setStatus(PaymentStatus.SUCCESS);

        // 3️⃣ Save payment
        PaymentEntity savedPayment = paymentRepository.save(payment);

        // 4️⃣ Update booking status
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        // 5️⃣ Map entity → DTO using ModelMapper
        PaymentResponseDTO response = modelMapper.map(savedPayment, PaymentResponseDTO.class);

        // Optional: If PaymentResponseDTO fields don't exactly match, set them manually
        // For example, if PaymentResponseDTO has amount instead of finalAmount:
        response.setAmount(savedPayment.getFinalAmount());

        return response;
    }

}
