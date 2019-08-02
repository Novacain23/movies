package com.example.scheduleservice.scheduleservice.service;


import com.example.scheduleservice.scheduleservice.exception.NoMovieWithThatName;
import com.example.scheduleservice.scheduleservice.model.Genre;
import com.example.scheduleservice.scheduleservice.model.Movie;
import com.example.scheduleservice.scheduleservice.model.MovieScheduleInfoWrapper;
import com.example.scheduleservice.scheduleservice.model.Region;
import com.example.scheduleservice.scheduleservice.model.ScheduleInfo;
import com.example.scheduleservice.scheduleservice.repository.ScheduleRepository;
import com.netflix.ribbon.proxy.annotation.Http;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.nio.charset.Charset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private RestTemplate restTemplate;

    private ModelMapper modelMapper = new ModelMapper();
    private static Logger log = LoggerFactory.getLogger(ScheduleService.class);


    @Transactional
    public void saveSchedule(ScheduleInfo scheduleInfo) {
        if (!checkIfScheduleSent(scheduleInfo)) {
            Movie retrievedMovie = retrieveMovieByName(scheduleInfo.getMovieName());
            scheduleInfo.setMovieId(retrievedMovie.getId());
            scheduleRepository.save(scheduleInfo);
            MovieScheduleInfoWrapper movieScheduleInfoWrapper = new MovieScheduleInfoWrapper(scheduleInfo, retrievedMovie);
            String url = "http://notification-service/submit/entry";
            sendMovieScheduleWrapper(movieScheduleInfoWrapper, url);
        } else {
            log.info("Schedule for movie with ID: " + scheduleInfo.getMovieId() + " has been sent arleady.");
        }
    }

    public void sendUpdatedMovie(Movie movie) {
        Optional<ScheduleInfo> retrievedSchedule = scheduleRepository.findById(movie.getId());
        if(retrievedSchedule.isPresent()){
            MovieScheduleInfoWrapper movieScheduleInfoWrapper = new MovieScheduleInfoWrapper(retrievedSchedule.get(),movie);
            String url = "http://notification-service/submit/entry/update";
            sendMovieScheduleWrapper(movieScheduleInfoWrapper,url);
            log.info("Sent updated movie.");
        } else {
            log.info("There is no schedule made for this updated movie.");
        }
    }

    public Movie retrieveMovieByName(String movieName) throws NoMovieWithThatName {
        return restTemplate.exchange("http://movies-service/movie/name/?movieName=" + movieName,HttpMethod.GET, null,Movie.class).getBody();
    }

    private Boolean checkIfScheduleSent(ScheduleInfo scheduleInfo){
        Optional<ScheduleInfo> retrievedSchedule = scheduleRepository.findByMovieName(scheduleInfo.getMovieName());
        return retrievedSchedule.isPresent() &&  retrievedSchedule.get().isSent();
    }

    private void sendMovieScheduleWrapper(MovieScheduleInfoWrapper movieScheduleInfoWrapper, String url) {
        HttpEntity<MovieScheduleInfoWrapper> entity = new HttpEntity<>(movieScheduleInfoWrapper);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
                scheduleRepository.setToSent(movieScheduleInfoWrapper.getMovie().getId());
        } else {
            log.debug("The response from the notification service isn't 2xx.");
        }
}
}
