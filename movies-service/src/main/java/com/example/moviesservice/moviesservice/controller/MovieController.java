package com.example.moviesservice.moviesservice.controller;


import com.example.moviesservice.moviesservice.model.Movie;
import com.example.moviesservice.moviesservice.model.MovieDTO;
import com.example.moviesservice.moviesservice.model.MovieScheduleWrapper;
import com.example.moviesservice.moviesservice.model.ScheduleInfo;
import com.example.moviesservice.moviesservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;


    @PostMapping(value = "/new",
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public Movie addMovie(@RequestBody MovieScheduleWrapper movieScheduleWrapper) {
        return movieService.addMovie(movieScheduleWrapper);
    }

    @PatchMapping(value="/{id}",
                  produces = MediaType.APPLICATION_JSON_VALUE)
    public Movie patchMovie(@RequestBody Map<String, Object> updates, @PathVariable int id) {
       return movieService.patchMovie(updates,id);
    }

    @GetMapping(value="/{id}")
    public Movie getMovieById(@PathVariable int id) {
        return movieService.getMovieById(id);
    }

}
