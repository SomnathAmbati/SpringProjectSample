package com.example.SpringProject.seating;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.SpringProject.show.Show;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {

    List<SeatEntity> findByShowId(Long showId);

}
