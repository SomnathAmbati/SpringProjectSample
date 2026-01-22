package com.example.SpringProject.seating;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<SeatingEntity, Long> {

    List<SeatingEntity> findByScreenId(Long screenId);
}
