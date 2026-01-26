package com.example.SpringProject.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/movies")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMovieController {
    @Autowired
    private MovieService movieService;

    @PostMapping
    public MovieEntity add(@RequestBody MovieDTO dto) {
        return movieService.addMovie(dto);
    }

    @PutMapping("/{id}")
    public MovieEntity update(@PathVariable Long id, @RequestBody MovieDTO dto) {
        return movieService.updateMovie(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }
}
