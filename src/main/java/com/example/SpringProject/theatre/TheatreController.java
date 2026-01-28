package com.example.SpringProject.theatre;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringProject.Exception.ICinemaException;

import jakarta.validation.Valid;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/theatres")
@Validated
public class TheatreController {
    
    @Autowired
    private TheatreService theatreService;
    
    @Autowired
    private Environment environment;

    @PostMapping
    public ResponseEntity<String> createTheatre(@Valid @RequestBody TheatreDTO dto) throws ICinemaException {
        theatreService.createTheatre(dto);
        String message = environment.getProperty("API.THEATRE_CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping
    public ResponseEntity<List<TheatreDTO>> getAllTheatres() throws ICinemaException {
        List<TheatreDTO> theatres = theatreService.getAllTheatres();
        return ResponseEntity.ok(theatres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheatreDTO> getTheatreById(@PathVariable Long id) throws ICinemaException {
        TheatreDTO theatre = theatreService.getTheatreById(id);
        return ResponseEntity.ok(theatre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTheatre(
            @PathVariable Long id,
            @Valid @RequestBody TheatreDTO dto) throws ICinemaException {
        
        theatreService.update(id, dto);
        String message = environment.getProperty("API.THEATRE_UPDATED");
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTheatre(@PathVariable Long id) throws ICinemaException {
        theatreService.delete(id);
        String message = environment.getProperty("API.THEATRE_DELETED");
        return ResponseEntity.ok(message);
    }
}
