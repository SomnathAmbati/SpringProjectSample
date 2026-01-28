package com.example.SpringProject.show;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringProject.Exception.ICinemaException;

import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/shows")
@Validated
public class ShowController {
    
    @Autowired
    private ShowService showService;
    
    @Autowired
    private Environment environment;

    @GetMapping
    public ResponseEntity<List<ShowDTO>> getShows(@RequestParam Long movieId) throws ICinemaException {
        List<ShowDTO> shows = showService.getShowsByMovie(movieId);
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/{showId}")
    public ResponseEntity<ShowDTO> getShowById(@PathVariable Long showId) throws ICinemaException {
        ShowDTO show = showService.getShowById(showId);
        return ResponseEntity.ok(show);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createShow(
            @RequestParam Long movieId,
            @RequestParam Long theatreId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime showTime) 
            throws ICinemaException {
        
        showService.createShowWithId(movieId, theatreId, showTime);
        String message = environment.getProperty("API.SHOW_CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @DeleteMapping("/{showId}")
    public ResponseEntity<String> deleteShow(@PathVariable Long showId) throws ICinemaException {
        showService.deleteShow(showId);
        String message = environment.getProperty("API.SHOW_DELETED");
        return ResponseEntity.ok(message);
    }
}
