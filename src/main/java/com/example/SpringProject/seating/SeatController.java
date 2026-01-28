package com.example.SpringProject.seating;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/seats")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @GetMapping("/{showId}")
    public List<SeatDTO> getSeatChart(@PathVariable Long showId) {
        return seatService.getSeatChart(showId);
    }

    @GetMapping("/by-ids")
    public List<SeatDTO> getSeatsByIds(
            @RequestParam List<Long> ids) {

        return seatService.getSeatsByIds(ids);
    }
}
