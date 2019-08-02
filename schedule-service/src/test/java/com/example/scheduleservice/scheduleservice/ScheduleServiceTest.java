package com.example.scheduleservice.scheduleservice;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.scheduleservice.scheduleservice.model.Movie;
import com.example.scheduleservice.scheduleservice.model.ScheduleInfo;
import com.example.scheduleservice.scheduleservice.repository.ScheduleRepository;
import com.example.scheduleservice.scheduleservice.service.ScheduleService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ScheduleService scheduleService = new ScheduleService();

    private ScheduleInfo scheduleInfo;

    private Logger logger;
    private ListAppender<ILoggingEvent> listAppender;
    private Movie movie;

    @Before
    public void init() {
        movie = new Movie();
        movie.setId(1);
        logger = (Logger) LoggerFactory.getLogger(ScheduleService.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        scheduleInfo = new ScheduleInfo();
        scheduleInfo.setMovieId(1);
        scheduleInfo.setSent(false);
        scheduleInfo.setMovieName("Inception");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveSchedule_whenScheduleSent_logProblem() {
        scheduleInfo.setSent(true);
        when(scheduleRepository.findByMovieName("Inception")).thenReturn(Optional.of(scheduleInfo));

        scheduleService.saveSchedule(scheduleInfo);
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals("Schedule for movie with ID: 1 has been sent arleady.", logsList.get(0).getMessage());

    }

    @Test
    public void saveSchedule_whenScheduleNotSentAndSendIsSuccessful_sendAndSetToSent() {
        when(scheduleRepository.findByMovieName("Inception")).thenReturn(Optional.of(scheduleInfo));
        ResponseEntity<Movie> movieResponse = new ResponseEntity<>(movie, HttpStatus.OK);
        when(restTemplate.exchange(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<Movie>>any(),
                ArgumentMatchers.<Class<Movie>>any())).thenReturn(movieResponse);
        when(scheduleRepository.save(scheduleInfo)).thenReturn(scheduleInfo);
//        ResponseEntity<String> stringResponse = new ResponseEntity<>(any(String.class) , HttpStatus.OK);
//        when(restTemplate.exchange(ArgumentMatchers.anyString(),
//                ArgumentMatchers.any(HttpMethod.class),
//                ArgumentMatchers.<HttpEntity<Movie>>any(),
//                ArgumentMatchers.<Class<String>>any())).thenReturn(stringResponse);
        doNothing().when(scheduleRepository).setToSent(1);

        scheduleService.saveSchedule(scheduleInfo);
        verify(scheduleRepository).setToSent(1);
    }

    @Test
    public void saveSchedule_whenScheduleNotSentAndSendIsNotSuccessful_logProblem() {
        when(scheduleRepository.findByMovieName("Inception")).thenReturn(Optional.of(scheduleInfo));
        ResponseEntity<Movie> movieResponse = new ResponseEntity<>(movie, HttpStatus.OK);
        when(restTemplate.exchange(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<Movie>>any(),
                ArgumentMatchers.<Class<Movie>>any())).thenReturn(movieResponse);
        when(scheduleRepository.save(scheduleInfo)).thenReturn(scheduleInfo);
        doNothing().when(scheduleRepository).setToSent(1);

        scheduleService.saveSchedule(scheduleInfo);
        verify(scheduleRepository).setToSent(1);
    }

    @Test
    public void sendUpdatedMovie_whenMovieIsFoundAndResponseIsPositive_setToSentAndLog() {
        when(scheduleRepository.findById(1)).thenReturn(Optional.of(scheduleInfo));

        ResponseEntity<String> stringResponse = new ResponseEntity<>("Received.",HttpStatus.OK);
        when(restTemplate.exchange(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<String>>any(),
                ArgumentMatchers.<Class<String>>any())).thenReturn(stringResponse);
        doNothing().when(scheduleRepository).setToSent(1);

        scheduleService.sendUpdatedMovie(movie);
        verify(scheduleRepository).setToSent(1);
    }
    //TODO:
    @Test
    public void sendUpdatedMovie_whenMovieIsFoundAndResponseIsNegative_log() {
        when(scheduleRepository.findById(1)).thenReturn(Optional.of(scheduleInfo));

        ResponseEntity<String> stringResponse = new ResponseEntity<>("Received.",HttpStatus.OK);
        when(restTemplate.exchange(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<String>>any(),
                ArgumentMatchers.<Class<String>>any())).thenReturn(stringResponse);
        doNothing().when(scheduleRepository).setToSent(1);

        List<ILoggingEvent> logsList = listAppender.list;

        scheduleService.sendUpdatedMovie(movie);
        verify(scheduleRepository).setToSent(1);
    }
}
