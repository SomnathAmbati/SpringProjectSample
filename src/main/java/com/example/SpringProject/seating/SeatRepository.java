package com.example.SpringProject.seating;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SeatRepository extends JpaRepository<SeatEntity, Long> {

    List<SeatEntity> findByShowId(Long showId);

    List<SeatEntity> findByShowIdAndSeatNumberIn(Long showId, List<String> seatNumbers);

    List<SeatEntity> findByIdIn(List<Long> ids);

}
