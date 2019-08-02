package com.example.moviesservice.moviesservice.controller;


import com.example.moviesservice.moviesservice.model.Movie;
import com.example.moviesservice.moviesservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/new",
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public Movie addMovie(@RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping(value="/{id}",
                  produces = MediaType.APPLICATION_JSON_VALUE)
    public Movie patchMovie(@RequestBody Map<String, Object> updates, @PathVariable int id) {
       return movieService.patchMovie(updates,id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value="/{id}")
    public Movie getMovieById(@PathVariable int id) {
        return movieService.getMovieById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value="/name")
    public Movie getMovieByName(@RequestParam String movieName) {
        return movieService.getMovieByName(movieName);
    }
}
