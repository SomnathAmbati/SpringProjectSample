package com.example.SpringProject.seating;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SpringProject.common.AppEnums;

public interface SeatStatusRepository extends JpaRepository<SeatStatusMapping, Long> {

    List<SeatStatusMapping> findByShowId(Long showId);
    
    List<SeatStatusMapping> findByShowIdAndStatus(Long showId, AppEnums.SeatStatus status);
}
