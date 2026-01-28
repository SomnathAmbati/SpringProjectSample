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

import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.seating.SeatDTO;
import com.example.SpringProject.seating.SeatEntity;
import com.example.SpringProject.seating.SeatRepository;
import com.example.SpringProject.seating.SeatServiceImpl;
import com.example.SpringProject.show.Show;
import com.example.SpringProject.show.ShowRepository;


@ExtendWith(MockitoExtension.class)
public class SeatServiceImplTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ShowRepository showRepository;

    @InjectMocks
    private SeatServiceImpl seatService;

    // ==================== GET SEAT CHART TESTS ====================

    @Test
    public void getSeatChartTestValid() throws ResourceNotFoundException {
        // Arrange
        Show show = new Show();
        show.setId(1L);

        SeatEntity seat1 = new SeatEntity();
        seat1.setId(1L);
        seat1.setSeatNumber("A1");
        seat1.setSeatType(AppEnums.SeatType.PREMIUM);
        seat1.setStatus(AppEnums.SeatStatus.AVAILABLE);
        seat1.setShow(show);

        SeatEntity seat2 = new SeatEntity();
        seat2.setId(2L);
        seat2.setSeatNumber("A2");
        seat2.setSeatType(AppEnums.SeatType.PREMIUM);
        seat2.setStatus(AppEnums.SeatStatus.BOOKED);
        seat2.setShow(show);

        SeatEntity seat3 = new SeatEntity();
        seat3.setId(3L);
        seat3.setSeatNumber("C1");
        seat3.setSeatType(AppEnums.SeatType.REGULAR);
        seat3.setStatus(AppEnums.SeatStatus.AVAILABLE);
        seat3.setShow(show);

        List<SeatEntity> seats = Arrays.asList(seat1, seat2, seat3);

        Mockito.when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        Mockito.when(seatRepository.findByShowId(1L)).thenReturn(seats);

        // Act
        List<SeatDTO> result = seatService.getSeatChart(1L);

        // Assert
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("A1", result.get(0).getSeatNumber());
        Assertions.assertEquals(AppEnums.SeatType.PREMIUM, result.get(0).getSeatType());
        Assertions.assertEquals(AppEnums.SeatStatus.AVAILABLE, result.get(0).getStatus());
        Mockito.verify(showRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(seatRepository, Mockito.times(1)).findByShowId(1L);
    }

    @Test
    public void getSeatChartTestShowNotFound() {
        // Arrange
        Mockito.when(showRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> seatService.getSeatChart(999L)
        );
        Assertions.assertEquals("Service.SHOW_NOT_FOUND", exception.getMessage());
        Mockito.verify(showRepository, Mockito.times(1)).findById(999L);
        Mockito.verify(seatRepository, Mockito.times(0)).findByShowId(Mockito.anyLong());
    }

    @Test
    public void getSeatChartTestEmptySeats() throws ResourceNotFoundException {
        // Arrange
        Show show = new Show();
        show.setId(1L);

        Mockito.when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        Mockito.when(seatRepository.findByShowId(1L)).thenReturn(Arrays.asList());

        // Act
        List<SeatDTO> result = seatService.getSeatChart(1L);

        // Assert
        Assertions.assertEquals(0, result.size());
        Mockito.verify(seatRepository, Mockito.times(1)).findByShowId(1L);
    }

    // ==================== CALCULATE PRICE TESTS ====================

    @Test
    public void calculatePriceTestValidMixedSeats() throws ResourceNotFoundException {
        // Arrange
        SeatEntity premiumSeat = new SeatEntity();
        premiumSeat.setId(1L);
        premiumSeat.setSeatType(AppEnums.SeatType.PREMIUM);

        SeatEntity regularSeat1 = new SeatEntity();
        regularSeat1.setId(2L);
        regularSeat1.setSeatType(AppEnums.SeatType.REGULAR);

        SeatEntity regularSeat2 = new SeatEntity();
        regularSeat2.setId(3L);
        regularSeat2.setSeatType(AppEnums.SeatType.REGULAR);

        List<Long> seatIds = Arrays.asList(1L, 2L, 3L);
        List<SeatEntity> seats = Arrays.asList(premiumSeat, regularSeat1, regularSeat2);

        Mockito.when(seatRepository.findAllById(seatIds)).thenReturn(seats);

        // Act
        double result = seatService.calculatePrice(seatIds);

        // Assert
        // 1 Premium (250) + 2 Regular (180 each) = 250 + 360 = 610
        Assertions.assertEquals(610.0, result, 0.01);
        Mockito.verify(seatRepository, Mockito.times(1)).findAllById(seatIds);
    }

    @Test
    public void calculatePriceTestAllPremiumSeats() throws ResourceNotFoundException {
        // Arrange
        SeatEntity premium1 = new SeatEntity();
        premium1.setId(1L);
        premium1.setSeatType(AppEnums.SeatType.PREMIUM);

        SeatEntity premium2 = new SeatEntity();
        premium2.setId(2L);
        premium2.setSeatType(AppEnums.SeatType.PREMIUM);

        List<Long> seatIds = Arrays.asList(1L, 2L);
        List<SeatEntity> seats = Arrays.asList(premium1, premium2);

        Mockito.when(seatRepository.findAllById(seatIds)).thenReturn(seats);

        // Act
        double result = seatService.calculatePrice(seatIds);

        // Assert
        // 2 Premium (250 each) = 500
        Assertions.assertEquals(500.0, result, 0.01);
        Mockito.verify(seatRepository, Mockito.times(1)).findAllById(seatIds);
    }

    @Test
    public void calculatePriceTestNullSeatIds() {
        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> seatService.calculatePrice(null)
        );
        Assertions.assertEquals("Service.NO_SEATS_SELECTED", exception.getMessage());
        Mockito.verify(seatRepository, Mockito.times(0)).findAllById(Mockito.anyList());
    }

    @Test
    public void calculatePriceTestEmptySeatIds() {
        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> seatService.calculatePrice(Arrays.asList())
        );
        Assertions.assertEquals("Service.NO_SEATS_SELECTED", exception.getMessage());
        Mockito.verify(seatRepository, Mockito.times(0)).findAllById(Mockito.anyList());
    }

    // ==================== GET SEATS BY IDS TESTS ====================

    @Test
    public void getSeatsByIdsTestValid() throws ResourceNotFoundException {
        // Arrange
        SeatEntity seat1 = new SeatEntity();
        seat1.setId(1L);
        seat1.setSeatNumber("A1");
        seat1.setSeatType(AppEnums.SeatType.PREMIUM);

        SeatEntity seat2 = new SeatEntity();
        seat2.setId(2L);
        seat2.setSeatNumber("A2");
        seat2.setSeatType(AppEnums.SeatType.PREMIUM);

        List<Long> seatIds = Arrays.asList(1L, 2L);
        List<SeatEntity> seats = Arrays.asList(seat1, seat2);

        Mockito.when(seatRepository.findByIdIn(seatIds)).thenReturn(seats);

        // Act
        List<SeatDTO> result = seatService.getSeatsByIds(seatIds);

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("A1", result.get(0).getSeatNumber());
        Assertions.assertEquals("A2", result.get(1).getSeatNumber());
        Mockito.verify(seatRepository, Mockito.times(1)).findByIdIn(seatIds);
    }

    @Test
    public void getSeatsByIdsTestNullIds() {
        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> seatService.getSeatsByIds(null)
        );
        Assertions.assertEquals("Service.NO_SEATS_SELECTED", exception.getMessage());
        Mockito.verify(seatRepository, Mockito.times(0)).findByIdIn(Mockito.anyList());
    }

    @Test
    public void getSeatsByIdsTestEmptyIds() {
        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> seatService.getSeatsByIds(Arrays.asList())
        );
        Assertions.assertEquals("Service.NO_SEATS_SELECTED", exception.getMessage());
        Mockito.verify(seatRepository, Mockito.times(0)).findByIdIn(Mockito.anyList());
    }

    @Test
    public void getSeatsByIdsTestNoSeatsFound() {
        // Arrange
        List<Long> seatIds = Arrays.asList(999L, 998L);
        Mockito.when(seatRepository.findByIdIn(seatIds)).thenReturn(Arrays.asList());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> seatService.getSeatsByIds(seatIds)
        );
        Assertions.assertEquals("Service.SEATS_NOT_FOUND", exception.getMessage());
        Mockito.verify(seatRepository, Mockito.times(1)).findByIdIn(seatIds);
    }
}
