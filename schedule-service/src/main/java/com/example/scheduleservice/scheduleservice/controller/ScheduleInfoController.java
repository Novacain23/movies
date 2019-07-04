package com.example.scheduleservice.scheduleservice.controller;


import com.example.scheduleservice.scheduleservice.model.ScheduleInfo;
import com.example.scheduleservice.scheduleservice.scheduler.Scheduler;
import com.example.scheduleservice.scheduleservice.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/schedule")
public class ScheduleInfoController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private Scheduler scheduler;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public void schedule(@RequestBody ScheduleInfo scheduleInfo) {
        scheduleService.schedule(scheduleInfo);
        System.out.println("Sent to service.");
    }

    @GetMapping(value="/test")
    public void test() {
        scheduler.send();
    }

}
