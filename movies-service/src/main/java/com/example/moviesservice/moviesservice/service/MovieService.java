package com.example.moviesservice.moviesservice.service;


import com.example.moviesservice.moviesservice.exception.MovieNameArleadyExists;
import com.example.moviesservice.moviesservice.exception.NoMovieWithThatId;
import com.example.moviesservice.moviesservice.model.Genre;
import com.example.moviesservice.moviesservice.model.Movie;
import com.example.moviesservice.moviesservice.model.MovieDTO;
import com.example.moviesservice.moviesservice.model.MovieScheduleWrapper;
import com.example.moviesservice.moviesservice.model.ScheduleInfo;
import com.example.moviesservice.moviesservice.repository.MovieRepository;
import org.apache.commons.lang.enums.EnumUtils;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder builder;

    ModelMapper mapper = new ModelMapper();

    @Transactional
    public Movie addMovie(MovieScheduleWrapper movieScheduleWrapper) {
        String movieName = movieScheduleWrapper.getMovie().getMovieName();
        Movie movieFromWrapper = movieScheduleWrapper.getMovie();
        Optional<Movie> retrieved = movieRepository.findByMovieName(movieName);
        if(retrieved.isPresent()) {
            throw new MovieNameArleadyExists("The name: " + movieName + " is arleady taken.");
        } else {
        Set<Genre> genres = movieFromWrapper.getGenres().stream()
                .filter(g -> Genre.contains(g.toString()))
                .collect(Collectors.toSet());
        movieFromWrapper.setGenres(genres);
        movieRepository.save(movieFromWrapper);
        Movie takenForId = movieRepository.findByMovieName(movieName).get();
        ScheduleInfo scheduleInfo = movieScheduleWrapper.getScheduleInfo();
        scheduleInfo.setMovieId(takenForId.getId());
        toSchedule(scheduleInfo);
        return takenForId;
        }
    }


    private void toSchedule(ScheduleInfo scheduleInfo) {
        HttpEntity<ScheduleInfo> entity = new HttpEntity<>(scheduleInfo);
        restTemplate.exchange("http://schedule-service/schedule", HttpMethod.POST, entity, ScheduleInfo.class);
        System.out.println("Notified.");
    }

    public Movie patchMovie(Map<String, Object> updates, int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isPresent()) {
            updates.forEach((k, v) -> {
                Field field = ReflectionUtils.findRequiredField(Movie.class, k);
                ReflectionUtils.setField(field,movie.get(), v);
            });
            return movieRepository.save(movie.get());
        } else {
            throw new NoMovieWithThatId("There is no movie with the ID of: " + id);
        }
    }

    public Movie getMovieById(int id) {
        Optional<Movie> retrieved = movieRepository.findById(id);
        if(retrieved.isPresent()) {
            return retrieved.get();
        } else {
            throw new NoMovieWithThatId("There is no movie with the ID of: " + id);
        }
    }
}
