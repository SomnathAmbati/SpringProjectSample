package com.example.SpringProject;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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

import com.example.SpringProject.Exception.BadRequestException;
import com.example.SpringProject.Exception.ConflictException;
import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.booking.Booking;
import com.example.SpringProject.booking.BookingDTO;
import com.example.SpringProject.booking.BookingRepository;
import com.example.SpringProject.booking.BookingServiceImpl;
import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.movie.MovieEntity;
import com.example.SpringProject.seating.SeatEntity;
import com.example.SpringProject.seating.SeatRepository;
import com.example.SpringProject.show.Show;
import com.example.SpringProject.show.ShowRepository;
import com.example.SpringProject.theatre.TheatreEntity;
import com.example.SpringProject.user.User;
    
@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
        @Mock
        private BookingRepository bookingRepository;
    
        @Mock
        private SeatRepository seatRepository;
    
        @Mock
        private ShowRepository showRepository;
    
        @InjectMocks
        private BookingServiceImpl bookingService;
    

         // ==================== BOOKING HISTORY VALID ====================
        @Test
        void getBookingsByUserIdValid() throws ResourceNotFoundException {

            Booking booking = new Booking();
            booking.setId(1L);

            Mockito.when(bookingRepository.findByUserIdOrderByBookingTimeDesc(1L))
                    .thenReturn(List.of(booking));

            List<Booking> result = bookingService.getBookingsByUserId(1L);

            Assertions.assertEquals(1, result.size());
            Mockito.verify(bookingRepository, Mockito.times(1))
                    .findByUserIdOrderByBookingTimeDesc(1L);
        }

        // ==================== BOOKING HISTORY INVALID ====================
        @Test
        void getBookingsByUserIdInvalid() {

            Mockito.when(bookingRepository.findByUserIdOrderByBookingTimeDesc(2L))
                    .thenReturn(Collections.emptyList());

            Assertions.assertThrows(
                    ResourceNotFoundException.class,
                    () -> bookingService.getBookingsByUserId(2L)
            );
        }


        // ==================== CREATE BOOKING TESTS ====================
    
        @Test
        public void createBookingTestValid() throws ResourceNotFoundException, BadRequestException, ConflictException {
            // Arrange
            User user = new User();
            user.setId(1L);
            user.setEmail("user@test.com");
    
            MovieEntity movie = new MovieEntity();
            movie.setId(1L);
            movie.setName("Inception");
    
            TheatreEntity theatre = new TheatreEntity();
            theatre.setId(1L);
            theatre.setName("PVR Cinemas");
    
            Show show = new Show();
            show.setId(1L);
            show.setMovie(movie);
            show.setTheatre(theatre);
    
            SeatEntity seat1 = new SeatEntity();
            seat1.setId(1L);
            seat1.setSeatNumber("A1");
            seat1.setSeatType(AppEnums.SeatType.PREMIUM);
            seat1.setStatus(AppEnums.SeatStatus.AVAILABLE);
            seat1.setShow(show);
    
            SeatEntity seat2 = new SeatEntity();
            seat2.setId(2L);
            seat2.setSeatNumber("A2");
            seat2.setSeatType(AppEnums.SeatType.REGULAR);
            seat2.setStatus(AppEnums.SeatStatus.AVAILABLE);
            seat2.setShow(show);
    
            List<Long> seatIds = Arrays.asList(1L, 2L);
            List<SeatEntity> seats = Arrays.asList(seat1, seat2);
    
            BookingDTO dto = new BookingDTO();
            dto.setShowId(1L);
            dto.setSeatIds(seatIds);
    
            Booking savedBooking = new Booking();
            savedBooking.setId(1L);
            savedBooking.setUser(user);
            savedBooking.setShow(show);
            savedBooking.setSeatNumbers("A1,A2");
            savedBooking.setTotalPrice(430.0); // 250 + 180
            savedBooking.setStatus(AppEnums.BookingStatus.INITIATED);
    
            Mockito.when(showRepository.findById(1L)).thenReturn(Optional.of(show));
            Mockito.when(seatRepository.findAllById(seatIds)).thenReturn(seats);
            Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(savedBooking);
    
            // Act
            Booking result = bookingService.createBooking(user, dto);
    
            // Assert
            Assertions.assertNotNull(result);
            Assertions.assertEquals("A1,A2", result.getSeatNumbers());
            Assertions.assertEquals(430.0, result.getTotalPrice(), 0.01);
            Assertions.assertEquals(AppEnums.BookingStatus.INITIATED, result.getStatus());
            Mockito.verify(showRepository, Mockito.times(1)).findById(1L);
            Mockito.verify(seatRepository, Mockito.times(1)).findAllById(seatIds);
            Mockito.verify(bookingRepository, Mockito.times(1)).save(Mockito.any(Booking.class));
        }
    
        @Test
        public void createBookingTestNullSeatIds() {
            // Arrange
            User user = new User();
            user.setId(1L);
    
            BookingDTO dto = new BookingDTO();
            dto.setShowId(1L);
            dto.setSeatIds(null);
    
            // Act & Assert
            BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingService.createBooking(user, dto)
            );
            Assertions.assertEquals("Service.NO_SEATS_SELECTED", exception.getMessage());
            Mockito.verify(showRepository, Mockito.times(0)).findById(Mockito.anyLong());
        }
    
        @Test
        public void createBookingTestEmptySeatIds() {
            // Arrange
            User user = new User();
            user.setId(1L);
    
            BookingDTO dto = new BookingDTO();
            dto.setShowId(1L);
            dto.setSeatIds(Arrays.asList());
    
            // Act & Assert
            BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingService.createBooking(user, dto)
            );
            Assertions.assertEquals("Service.NO_SEATS_SELECTED", exception.getMessage());
            Mockito.verify(showRepository, Mockito.times(0)).findById(Mockito.anyLong());
        }
    
        @Test
        public void createBookingTestShowNotFound() {
            // Arrange
            User user = new User();
            user.setId(1L);
    
            BookingDTO dto = new BookingDTO();
            dto.setShowId(999L);
            dto.setSeatIds(Arrays.asList(1L, 2L));
    
            Mockito.when(showRepository.findById(999L)).thenReturn(Optional.empty());
    
            // Act & Assert
            ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.createBooking(user, dto)
            );
            Assertions.assertEquals("Service.SHOW_NOT_FOUND", exception.getMessage());
            Mockito.verify(showRepository, Mockito.times(1)).findById(999L);
            Mockito.verify(seatRepository, Mockito.times(0)).findAllById(Mockito.anyList());
        }
    
        @Test
        public void createBookingTestSeatsNotFound() {
            // Arrange
            User user = new User();
            user.setId(1L);
    
            Show show = new Show();
            show.setId(1L);
    
            BookingDTO dto = new BookingDTO();
            dto.setShowId(1L);
            dto.setSeatIds(Arrays.asList(999L, 998L));
    
            Mockito.when(showRepository.findById(1L)).thenReturn(Optional.of(show));
            Mockito.when(seatRepository.findAllById(dto.getSeatIds())).thenReturn(Arrays.asList());
    
            // Act & Assert
            ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.createBooking(user, dto)
            );
            Assertions.assertEquals("Service.SEATS_NOT_FOUND", exception.getMessage());
            Mockito.verify(seatRepository, Mockito.times(1)).findAllById(dto.getSeatIds());
        }
    
        @Test
        public void createBookingTestPartialSeatsFound() {
            // Arrange
            User user = new User();
            user.setId(1L);
    
            Show show = new Show();
            show.setId(1L);
    
            SeatEntity seat1 = new SeatEntity();
            seat1.setId(1L);
            seat1.setSeatNumber("A1");
    
            // Only 1 seat found, but 2 requested
            List<Long> requestedIds = Arrays.asList(1L, 2L);
            List<SeatEntity> foundSeats = Arrays.asList(seat1);
    
            BookingDTO dto = new BookingDTO();
            dto.setShowId(1L);
            dto.setSeatIds(requestedIds);
    
            Mockito.when(showRepository.findById(1L)).thenReturn(Optional.of(show));
            Mockito.when(seatRepository.findAllById(requestedIds)).thenReturn(foundSeats);
    
            // Act & Assert
            ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.createBooking(user, dto)
            );
            Assertions.assertEquals("Service.SEATS_NOT_FOUND", exception.getMessage());
        }
    
        @Test
        public void createBookingTestSeatAlreadyBooked() {
            // Arrange
            User user = new User();
            user.setId(1L);
    
            Show show = new Show();
            show.setId(1L);
    
            SeatEntity seat1 = new SeatEntity();
            seat1.setId(1L);
            seat1.setSeatNumber("A1");
            seat1.setSeatType(AppEnums.SeatType.PREMIUM);
            seat1.setStatus(AppEnums.SeatStatus.AVAILABLE);
    
            SeatEntity seat2 = new SeatEntity();
            seat2.setId(2L);
            seat2.setSeatNumber("A2");
            seat2.setSeatType(AppEnums.SeatType.REGULAR);
            seat2.setStatus(AppEnums.SeatStatus.BOOKED); // Already booked!
    
            List<Long> seatIds = Arrays.asList(1L, 2L);
            List<SeatEntity> seats = Arrays.asList(seat1, seat2);
    
            BookingDTO dto = new BookingDTO();
            dto.setShowId(1L);
            dto.setSeatIds(seatIds);
    
            Mockito.when(showRepository.findById(1L)).thenReturn(Optional.of(show));
            Mockito.when(seatRepository.findAllById(seatIds)).thenReturn(seats);
    
            // Act & Assert
            ConflictException exception = Assertions.assertThrows(
                ConflictException.class,
                () -> bookingService.createBooking(user, dto)
            );
            Assertions.assertEquals("Service.SEAT_ALREADY_BOOKED", exception.getMessage());
            Mockito.verify(bookingRepository, Mockito.times(0)).save(Mockito.any());
        }
    
        @Test
        public void createBookingTestPriceCalculationAllPremium() 
                throws ResourceNotFoundException, BadRequestException, ConflictException {
            // Arrange
            User user = new User();
            user.setId(1L);
    
            Show show = new Show();
            show.setId(1L);
    
            SeatEntity seat1 = new SeatEntity();
            seat1.setId(1L);
            seat1.setSeatNumber("A1");
            seat1.setSeatType(AppEnums.SeatType.PREMIUM);
            seat1.setStatus(AppEnums.SeatStatus.AVAILABLE);
    
            SeatEntity seat2 = new SeatEntity();
            seat2.setId(2L);
            seat2.setSeatNumber("A2");
            seat2.setSeatType(AppEnums.SeatType.PREMIUM);
            seat2.setStatus(AppEnums.SeatStatus.AVAILABLE);
    
            List<Long> seatIds = Arrays.asList(1L, 2L);
            List<SeatEntity> seats = Arrays.asList(seat1, seat2);
    
            BookingDTO dto = new BookingDTO();
            dto.setShowId(1L);
            dto.setSeatIds(seatIds);
    
            Booking savedBooking = new Booking();
            savedBooking.setId(1L);
            savedBooking.setTotalPrice(500.0); // 2 x 250
    
            Mockito.when(showRepository.findById(1L)).thenReturn(Optional.of(show));
            Mockito.when(seatRepository.findAllById(seatIds)).thenReturn(seats);
            Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(savedBooking);
    
            // Act
            Booking result = bookingService.createBooking(user, dto);
    
            // Assert
            Assertions.assertEquals(500.0, result.getTotalPrice(), 0.01);
        }
    
        

        // ==================== GET BOOKING BY ID TESTS ====================
    
        @Test
        public void getBookingByIdTestValid() throws ResourceNotFoundException {
            // Arrange
            Booking booking = new Booking();
            booking.setId(1L);
            booking.setSeatNumbers("A1,A2");
            booking.setTotalPrice(430.0);
            booking.setStatus(AppEnums.BookingStatus.CONFIRMED);
            booking.setBookingTime(LocalDateTime.now());
    
            Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
    
            // Act
            Booking result = bookingService.getBookingById(1L);
    
            // Assert
            Assertions.assertNotNull(result);
            Assertions.assertEquals("A1,A2", result.getSeatNumbers());
            Assertions.assertEquals(430.0, result.getTotalPrice(), 0.01);
            Assertions.assertEquals(AppEnums.BookingStatus.CONFIRMED, result.getStatus());
            Mockito.verify(bookingRepository, Mockito.times(1)).findById(1L);
        }
    
        @Test
        public void getBookingByIdTestInvalidId() {
            // Arrange
            Mockito.when(bookingRepository.findById(999L)).thenReturn(Optional.empty());
    
            // Act & Assert
            ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.getBookingById(999L)
            );
            Assertions.assertEquals("Service.BOOKING_NOT_FOUND", exception.getMessage());
            Mockito.verify(bookingRepository, Mockito.times(1)).findById(999L);
        }
    }


