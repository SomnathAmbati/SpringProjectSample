package com.example.SpringProject.theatre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringProject.Exception.ResourceNotFoundException;


@RestController
@RequestMapping("/api/admin/theatres")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTheatreController {

    @Autowired
    private TheatreService theatreService;

    @PostMapping
    public TheatreEntity create(@RequestBody TheatreDTO dto) {
        return theatreService.create(dto);
    }

    @PutMapping("/{id}")
    public TheatreEntity update(@PathVariable Long id, @RequestBody TheatreDTO dto) throws ResourceNotFoundException {
        return theatreService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws ResourceNotFoundException {
        theatreService.delete(id);
    }
}

