package com.example.SpringProject.seating;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SpringProject.common.AppEnums;

public interface ShowSeatPriceRepository extends JpaRepository<ShowSeatPriceEntity, Long> {

    Optional<ShowSeatPriceEntity> findByShowIdAndSeatType(Long showId, AppEnums.SeatType seatType);
}