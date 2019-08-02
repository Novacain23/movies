package com.example.scheduleservice.scheduleservice.controller;


import com.example.scheduleservice.scheduleservice.model.Movie;
import com.example.scheduleservice.scheduleservice.model.ScheduleInfo;
import com.example.scheduleservice.scheduleservice.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Scheduler;

@RestController
@RequestMapping(value = "/schedule")
public class ScheduleInfoController {

    @Autowired
    private ScheduleService scheduleService;


    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(value="/save",consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveSchedule(@RequestBody ScheduleInfo scheduleInfo) {
        scheduleService.saveSchedule(scheduleInfo);
        System.out.println("Sent to service.");
    }

    @GetMapping(value="/getmovie/{movieName}")
    public Movie getMovieByName(@PathVariable String movieName) {
        return scheduleService.retrieveMovieByName(movieName);
    }

    @PostMapping(value="/update", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public void receiveAndSendUpdatedMovie(@RequestBody Movie movie) {
        scheduleService.sendUpdatedMovie(movie);
    }


}
