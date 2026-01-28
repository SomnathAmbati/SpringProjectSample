package com.example.SpringProject.seating;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringProject.Exception.ICinemaException;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/seats")
@Validated
public class SeatController {
    
    @Autowired
    private SeatService seatService;

    @GetMapping("/{showId}")
    public ResponseEntity<List<SeatDTO>> getSeatChart(@PathVariable Long showId) throws ICinemaException {
        List<SeatDTO> seats = seatService.getSeatChart(showId);
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<SeatDTO>> getSeatsByIds(@RequestParam List<Long> ids) throws ICinemaException {
        List<SeatDTO> seats = seatService.getSeatsByIds(ids);
        return ResponseEntity.ok(seats);
    }
}

