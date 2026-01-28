package com.example.SpringProject.show;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/admin/shows")
@PreAuthorize("hasRole('ADMIN')")
public class AdminShowController {
    @Autowired
    private ShowService service;

    @PostMapping
    public Show create(@RequestParam Long movieId,
                       @RequestParam Long theatreId,
                       @RequestParam String showTime) {
        return service.createShowWithId(
                movieId,
                theatreId,
                LocalDateTime.parse(showTime));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteShow(id);
    }
}

