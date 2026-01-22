package com.example.SpringProject.theatre;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreenRepository extends JpaRepository<ScreenEntity, Long> {

    List<ScreenEntity> findByTheatreId(Long theatreId);
}

