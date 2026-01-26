package com.example.SpringProject.theatre;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {
    @Autowired
    private TheatreService theatreService;

    // ADMIN – create theatre
    @PostMapping
    public TheatreDTO create(@RequestBody TheatreDTO dto) {
        return theatreService.createTheatre(dto);
    }

    // PUBLIC – list theatres
    @GetMapping
    public List<TheatreDTO> getAll() {
        return theatreService.getAllTheatres();
    }

    @GetMapping("/{id}")
    public TheatreDTO getById(@PathVariable Long id) {
        return theatreService.getTheatreById(id);
    }
}
