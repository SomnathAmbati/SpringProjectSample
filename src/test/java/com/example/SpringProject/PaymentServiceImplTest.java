package com.example.SpringProject;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.SpringProject.Exception.BusinessException;
import com.example.SpringProject.Exception.ConflictException;
import com.example.SpringProject.Exception.PaymentException;
import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.booking.Booking;
import com.example.SpringProject.booking.BookingRepository;
import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.movie.MovieEntity;
import com.example.SpringProject.payment.PaymentDTO;
import com.example.SpringProject.payment.PaymentEntity;
import com.example.SpringProject.payment.PaymentRepository;
import com.example.SpringProject.payment.PaymentResponseDTO;
import com.example.SpringProject.payment.PaymentServiceImpl;
import com.example.SpringProject.seating.SeatEntity;
import com.example.SpringProject.seating.SeatRepository;
import com.example.SpringProject.show.Show;
import com.example.SpringProject.theatre.TheatreEntity;
import com.example.SpringProject.user.User;
    
@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    
        @Mock
        private PaymentRepository paymentRepository;
    
        @Mock
        private BookingRepository bookingRepository;
    
        @Mock
        private SeatRepository seatRepository;
    
        @InjectMocks
        private PaymentServiceImpl paymentService;
    
        // ==================== PROCESS PAYMENT TESTS ====================
    
        @Test
        public void processPaymentTestValid() 
                throws ResourceNotFoundException, BusinessException, ConflictException, PaymentException {
            // Arrange
            User user = new User();
            user.setId(1L);
    
            MovieEntity movie = new MovieEntity();
            movie.setId(1L);
            movie.setName("Inception");
    
            TheatreEntity theatre = new TheatreEntity();
            theatre.setId(1L);
    
            Show show = new Show();
            show.setId(1L);
            show.setMovie(movie);
            show.setTheatre(theatre);
    
            Booking booking = new Booking();
            booking.setId(1L);
            booking.setUser(user);
            booking.setShow(show);
            booking.setSeatNumbers("A1,A2");
            booking.setTotalPrice(430.0);
            booking.setStatus(AppEnums.BookingStatus.INITIATED);
    
            SeatEntity seat1 = new SeatEntity();
            seat1.setId(1L);
            seat1.setSeatNumber("A1");
            seat1.setStatus(AppEnums.SeatStatus.AVAILABLE);
            seat1.setShow(show);
    
            SeatEntity seat2 = new SeatEntity();
            seat2.setId(2L);
            seat2.setSeatNumber("A2");
            seat2.setStatus(AppEnums.SeatStatus.AVAILABLE);
            seat2.setShow(show);
    
            List<String> seatNumbers = Arrays.asList("A1", "A2");
            List<SeatEntity> seats = Arrays.asList(seat1, seat2);
    
            PaymentDTO dto = new PaymentDTO();
            dto.setBookingId(1L);
            dto.setMode(AppEnums.PaymentMode.CREDIT);
    
            PaymentEntity savedPayment = new PaymentEntity();
            savedPayment.setId(1L);
            savedPayment.setBooking(booking);
            savedPayment.setMode(AppEnums.PaymentMode.CREDIT);
            savedPayment.setFinalAmount(430.0);
            savedPayment.setStatus(AppEnums.PaymentStatus.SUCCESS);
    
            Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            Mockito.when(seatRepository.findByShowIdAndSeatNumberIn(1L, seatNumbers)).thenReturn(seats);
            Mockito.when(paymentRepository.save(Mockito.any(PaymentEntity.class))).thenReturn(savedPayment);
            Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);
    
            // Act
            PaymentResponseDTO result = paymentService.processPayment(dto);
    
            // Assert
            Assertions.assertNotNull(result);
            Assertions.assertEquals(430.0, result.getAmount(), 0.01);
            Mockito.verify(bookingRepository, Mockito.times(1)).findById(1L);
            Mockito.verify(seatRepository, Mockito.times(1)).saveAll(seats);
            Mockito.verify(paymentRepository, Mockito.times(1)).save(Mockito.any(PaymentEntity.class));
            Mockito.verify(bookingRepository, Mockito.times(1)).save(booking);
            
            // Verify seats are marked as BOOKED
            Assertions.assertEquals(AppEnums.SeatStatus.BOOKED, seat1.getStatus());
            Assertions.assertEquals(AppEnums.SeatStatus.BOOKED, seat2.getStatus());
            
            // Verify booking status is CONFIRMED
            Assertions.assertEquals(AppEnums.BookingStatus.CONFIRMED, booking.getStatus());
        }
    
        @Test
        public void processPaymentTestBookingNotFound() {
            // Arrange
            PaymentDTO dto = new PaymentDTO();
            dto.setBookingId(999L);
            dto.setMode(AppEnums.PaymentMode.CREDIT);
    
            Mockito.when(bookingRepository.findById(999L)).thenReturn(Optional.empty());
    
            // Act & Assert
            ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> paymentService.processPayment(dto)
            );
            Assertions.assertEquals("Service.BOOKING_NOT_FOUND", exception.getMessage());
            Mockito.verify(bookingRepository, Mockito.times(1)).findById(999L);
            Mockito.verify(paymentRepository, Mockito.times(0)).save(Mockito.any());
        }
    
        @Test
        public void processPaymentTestInvalidBookingState() {
            // Arrange
            Booking booking = new Booking();
            booking.setId(1L);
            booking.setStatus(AppEnums.BookingStatus.CONFIRMED); // Already confirmed!
    
            PaymentDTO dto = new PaymentDTO();
            dto.setBookingId(1L);
            dto.setMode(AppEnums.PaymentMode.CREDIT);
    
            Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
    
            // Act & Assert
            BusinessException exception = Assertions.assertThrows(
                BusinessException.class,
                () -> paymentService.processPayment(dto)
            );
            Assertions.assertEquals("Service.INVALID_BOOKING_STATE", exception.getMessage());
            Mockito.verify(bookingRepository, Mockito.times(1)).findById(1L);
            Mockito.verify(seatRepository, Mockito.times(0)).findByShowIdAndSeatNumberIn(
                Mockito.anyLong(), Mockito.anyList()
            );
        }
    
        @Test
        public void processPaymentTestBookingCancelled() {
            // Arrange
            Booking booking = new Booking();
            booking.setId(1L);
            booking.setStatus(AppEnums.BookingStatus.CANCELLED);
    
            PaymentDTO dto = new PaymentDTO();
            dto.setBookingId(1L);
            dto.setMode(AppEnums.PaymentMode.DEBIT);
    
            Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
    
            // Act & Assert
            BusinessException exception = Assertions.assertThrows(
                BusinessException.class,
                () -> paymentService.processPayment(dto)
            );
            Assertions.assertEquals("Service.INVALID_BOOKING_STATE", exception.getMessage());
        }
    
        @Test
        public void processPaymentTestSeatsNotFound() {
            // Arrange
            Show show = new Show();
            show.setId(1L);
    
            Booking booking = new Booking();
            booking.setId(1L);
            booking.setShow(show);
            booking.setSeatNumbers("A1,A2");
            booking.setStatus(AppEnums.BookingStatus.INITIATED);
    
            PaymentDTO dto = new PaymentDTO();
            dto.setBookingId(1L);
            dto.setMode(AppEnums.PaymentMode.CREDIT);
    
            List<String> seatNumbers = Arrays.asList("A1", "A2");
    
            Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            Mockito.when(seatRepository.findByShowIdAndSeatNumberIn(1L, seatNumbers))
                .thenReturn(Arrays.asList()); // No seats found
    
            // Act & Assert
            ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> paymentService.processPayment(dto)
            );
            Assertions.assertEquals("Service.SEATS_NOT_FOUND", exception.getMessage());
            Mockito.verify(paymentRepository, Mockito.times(0)).save(Mockito.any());
        }
    
        @Test
        public void processPaymentTestSeatAlreadyBooked() {
            // Arrange
            Show show = new Show();
            show.setId(1L);
    
            Booking booking = new Booking();
            booking.setId(1L);
            booking.setShow(show);
            booking.setSeatNumbers("A1,A2");
            booking.setStatus(AppEnums.BookingStatus.INITIATED);
    
            SeatEntity seat1 = new SeatEntity();
            seat1.setId(1L);
            seat1.setSeatNumber("A1");
            seat1.setStatus(AppEnums.SeatStatus.AVAILABLE);
    
            SeatEntity seat2 = new SeatEntity();
            seat2.setId(2L);
            seat2.setSeatNumber("A2");
            seat2.setStatus(AppEnums.SeatStatus.BOOKED); // Already booked by someone else!
    
            List<String> seatNumbers = Arrays.asList("A1", "A2");
            List<SeatEntity> seats = Arrays.asList(seat1, seat2);
    
            PaymentDTO dto = new PaymentDTO();
            dto.setBookingId(1L);
            dto.setMode(AppEnums.PaymentMode.CREDIT);
    
            Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            Mockito.when(seatRepository.findByShowIdAndSeatNumberIn(1L, seatNumbers)).thenReturn(seats);
    
            // Act & Assert
            ConflictException exception = Assertions.assertThrows(
                ConflictException.class,
                () -> paymentService.processPayment(dto)
            );
            Assertions.assertEquals("Service.SEAT_ALREADY_BOOKED", exception.getMessage());
            Mockito.verify(seatRepository, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(paymentRepository, Mockito.times(0)).save(Mockito.any());
        }
    
        @Test
        public void processPaymentTestPaymentSaveFailure() {
            // Arrange
            Show show = new Show();
            show.setId(1L);
    
            Booking booking = new Booking();
            booking.setId(1L);
            booking.setShow(show);
            booking.setSeatNumbers("A1,A2");
            booking.setTotalPrice(430.0);
            booking.setStatus(AppEnums.BookingStatus.INITIATED);
    
            SeatEntity seat1 = new SeatEntity();
            seat1.setId(1L);
            seat1.setSeatNumber("A1");
            seat1.setStatus(AppEnums.SeatStatus.AVAILABLE);
    
            SeatEntity seat2 = new SeatEntity();
            seat2.setId(2L);
            seat2.setSeatNumber("A2");
            seat2.setStatus(AppEnums.SeatStatus.AVAILABLE);
    
            List<String> seatNumbers = Arrays.asList("A1", "A2");
            List<SeatEntity> seats = Arrays.asList(seat1, seat2);
    
            PaymentDTO dto = new PaymentDTO();
            dto.setBookingId(1L);
            dto.setMode(AppEnums.PaymentMode.CREDIT);
    
            Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            Mockito.when(seatRepository.findByShowIdAndSeatNumberIn(1L, seatNumbers)).thenReturn(seats);
            Mockito.when(paymentRepository.save(Mockito.any(PaymentEntity.class)))
                .thenThrow(new RuntimeException("Database error"));
    
            // Act & Assert
            PaymentException exception = Assertions.assertThrows(
                PaymentException.class,
                () -> paymentService.processPayment(dto)
            );
            Assertions.assertEquals("Service.PAYMENT_FAILED", exception.getMessage());
            
            // Verify rollback - seats should be available again
            Mockito.verify(seatRepository, Mockito.times(2)).saveAll(seats);
            Assertions.assertEquals(AppEnums.SeatStatus.AVAILABLE, seat1.getStatus());
            Assertions.assertEquals(AppEnums.SeatStatus.AVAILABLE, seat2.getStatus());
        }
    
        @Test
        public void processPaymentTestSeatStatusTransition() 
                throws ResourceNotFoundException, BusinessException, ConflictException, PaymentException {
            // Arrange
            Show show = new Show();
            show.setId(1L);
    
            Booking booking = new Booking();
            booking.setId(1L);
            booking.setShow(show);
            booking.setSeatNumbers("A1");
            booking.setTotalPrice(250.0);
            booking.setStatus(AppEnums.BookingStatus.INITIATED);
    
            SeatEntity seat = new SeatEntity();
            seat.setId(1L);
            seat.setSeatNumber("A1");
            seat.setStatus(AppEnums.SeatStatus.AVAILABLE);
    
            List<String> seatNumbers = Arrays.asList("A1");
            List<SeatEntity> seats = Arrays.asList(seat);
    
            PaymentDTO dto = new PaymentDTO();
            dto.setBookingId(1L);
            dto.setMode(AppEnums.PaymentMode.DEBIT);
    
            PaymentEntity savedPayment = new PaymentEntity();
            savedPayment.setId(1L);
            savedPayment.setFinalAmount(250.0);
            savedPayment.setStatus(AppEnums.PaymentStatus.SUCCESS);
    
            Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            Mockito.when(seatRepository.findByShowIdAndSeatNumberIn(1L, seatNumbers)).thenReturn(seats);
            Mockito.when(paymentRepository.save(Mockito.any(PaymentEntity.class))).thenReturn(savedPayment);
    
            // Act
            paymentService.processPayment(dto);
    
            // Assert - Seat should transition from AVAILABLE to BOOKED
            Assertions.assertEquals(AppEnums.SeatStatus.BOOKED, seat.getStatus());
            Mockito.verify(seatRepository, Mockito.times(1)).saveAll(seats);
        }
    
        @Test
        public void processPaymentTestBookingStatusTransition() 
                throws ResourceNotFoundException, BusinessException, ConflictException, PaymentException {
            // Arrange
            Show show = new Show();
            show.setId(1L);
    
            Booking booking = new Booking();
            booking.setId(1L);
            booking.setShow(show);
            booking.setSeatNumbers("A1");
            booking.setTotalPrice(250.0);
            booking.setStatus(AppEnums.BookingStatus.INITIATED);
    
            SeatEntity seat = new SeatEntity();
            seat.setId(1L);
            seat.setSeatNumber("A1");
            seat.setStatus(AppEnums.SeatStatus.AVAILABLE);
    
            List<String> seatNumbers = Arrays.asList("A1");
            List<SeatEntity> seats = Arrays.asList(seat);
    
            PaymentDTO dto = new PaymentDTO();
            dto.setBookingId(1L);
            dto.setMode(AppEnums.PaymentMode.CREDIT);
    
            PaymentEntity savedPayment = new PaymentEntity();
            savedPayment.setId(1L);
            savedPayment.setFinalAmount(250.0);
    
            Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            Mockito.when(seatRepository.findByShowIdAndSeatNumberIn(1L, seatNumbers)).thenReturn(seats);
            Mockito.when(paymentRepository.save(Mockito.any(PaymentEntity.class))).thenReturn(savedPayment);
            Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);
    
            // Act
            paymentService.processPayment(dto);
    
            // Assert - Booking should transition from INITIATED to CONFIRMED
            Assertions.assertEquals(AppEnums.BookingStatus.CONFIRMED, booking.getStatus());
            Mockito.verify(bookingRepository, Mockito.times(1)).save(booking);
        }
}