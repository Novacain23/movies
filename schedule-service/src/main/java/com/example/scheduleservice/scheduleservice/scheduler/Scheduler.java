package com.example.scheduleservice.scheduleservice.scheduler;

import com.example.scheduleservice.scheduleservice.model.Movie;
import com.example.scheduleservice.scheduleservice.model.MovieDTO;
import com.example.scheduleservice.scheduleservice.model.MovieDTOScheduleInfoDTOWrapper;
import com.example.scheduleservice.scheduleservice.model.MovieScheduleInfoWrapper;
import com.example.scheduleservice.scheduleservice.model.ScheduleInfo;
import com.example.scheduleservice.scheduleservice.model.ScheduleInfoDTO;
import com.example.scheduleservice.scheduleservice.repository.ScheduleRepository;
import com.example.scheduleservice.scheduleservice.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@EnableScheduling
public class Scheduler {


    @Autowired
    private ScheduleService scheduleService;


    @Scheduled(fixedRate = 360000)
    public void send() {
        System.out.println("Schedule met.");
        List<MovieScheduleInfoWrapper> retrievedList = scheduleService.getCloseToReleaseScheduleInfoAndMovieList();
        if(!retrievedList.isEmpty()) {
            List<Integer> idsToBeSetToSent = new ArrayList<>();
            retrievedList.forEach(e -> {
                String responseMessage = scheduleService.templateMessage(e);
                HttpEntity<String> entity = new HttpEntity<>(responseMessage);
                String response = scheduleService.sendEntity(entity);
                if(response.startsWith("Received.")) {
                    idsToBeSetToSent.add(e.getMovie().getId());
                }
            });
            scheduleService.setToSent(idsToBeSetToSent);
        }

    }
}
