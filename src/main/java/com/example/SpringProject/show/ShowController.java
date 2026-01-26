package com.example.SpringProject.show;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
 
