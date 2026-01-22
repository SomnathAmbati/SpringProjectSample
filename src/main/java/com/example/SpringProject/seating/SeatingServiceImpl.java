package com.example.SpringProject.seating;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.theatre.ScreenEntity;
import com.example.SpringProject.theatre.ScreenRepository;
import com.example.SpringProject.theatre.ShowEntity;
import com.example.SpringProject.theatre.ShowRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatingServiceImpl implements SeatingService {
    @Autowired
    private final SeatRepository seatRepository;
    @Autowired
    private final ScreenRepository screenRepository;
    @Autowired
    private final SeatStatusRepository seatStatusRepository;
    @Autowired
    private final ShowSeatPriceRepository showSeatPriceRepository;
    @Autowired
    private final ShowRepository showRepository;

    private ModelMapper modelMapper = new ModelMapper();
    // -------- ADMIN --------

    @Override
    public SeatingEntity addSeat(SeatingDTO seat) {
        ScreenEntity screen = screenRepository.findById(seat.getScreen().getId())
        .orElseThrow(() -> new RuntimeException("Screen not found"));
        
        SeatingEntity se = modelMapper.map(seat, SeatingEntity.class);
        se.setScreen(screen);
        return seatRepository.save(se);
    }

    @Override
    public ShowSeatPriceEntity setSeatPrice(Long showId, AppEnums.SeatType seatType, Double price) {
        ShowEntity show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        ShowSeatPriceEntity seatPrice = new ShowSeatPriceEntity();
        seatPrice.setShow(show);
        seatPrice.setSeatType(seatType);
        seatPrice.setPrice(price);

        return showSeatPriceRepository.save(seatPrice);
    }

    // -------- USER --------

    @Override
    public List<SeatStatusMapping> getSeatChart(Long showId) {
        return seatStatusRepository.findByShowId(showId);
    }

    @Override
    public void lockSeats(Long showId, List<Long> seatIds) {

        List<SeatStatusMapping> mappings = seatStatusRepository.findByShowId(showId);

        for (SeatStatusMapping mapping : mappings) {
            if (seatIds.contains(mapping.getSeat().getId())) {

                if (mapping.getStatus() != AppEnums.SeatStatus.AVAILABLE) {
                    throw new RuntimeException("Seat already booked");
                }

                mapping.setStatus(AppEnums.SeatStatus.SELECTED);
            }
        }

        seatStatusRepository.saveAll(mappings);
    }

    @Override
    public double calculateTotalPrice(Long showId, List<Long> seatIds) {

        double total = 0;

        for (Long seatId : seatIds) {
            SeatingEntity seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            ShowSeatPriceEntity price = showSeatPriceRepository
                    .findByShowIdAndSeatType(showId, seat.getSeatType())
                    .orElseThrow(() -> new RuntimeException("Price not configured"));

            total += price.getPrice();
        }
        return total;
    }
}