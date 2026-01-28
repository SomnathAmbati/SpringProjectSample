package com.example.SpringProject.show;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/shows")
public class ShowController {
    @Autowired
    private ShowService showService;

    @GetMapping
    public List<ShowDTO> getShows(@RequestParam Long movieId) {
        return showService.getShowsByMovie(movieId);
    }
    
    @GetMapping("/{showId}")
    public ShowDTO getShowById(@PathVariable Long showId) {
    		return showService.getShowById(showId);
    }

    /*
movieId=1
theatreId=2
showTime=2026-01-28T18:30:00
*/
// http://localhost:8080/api/shows/create?movieId=1&theatreId=2&showTime=2026-01-28T18:30:00
    @PostMapping("/create")
    public ResponseEntity<Show> createShow(
            @RequestParam Long movieId,
            @RequestParam Long theatreId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime showTime) {

        Show createdShow =
                showService.createShowWithId(movieId, theatreId, showTime);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdShow);
    }
}
 
