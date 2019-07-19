package com.example.moviesservice.moviesservice.service;


import com.example.moviesservice.moviesservice.exception.MovieNameArleadyExists;
import com.example.moviesservice.moviesservice.exception.NoMovieWithThatId;
import com.example.moviesservice.moviesservice.exception.NoMovieWithThatName;
import com.example.moviesservice.moviesservice.model.Genre;
import com.example.moviesservice.moviesservice.model.Movie;
import com.example.moviesservice.moviesservice.repository.MovieRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
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


    @Transactional
    @ResponseStatus(value = HttpStatus.CREATED)
    public Movie addMovie(Movie movie) {
        String movieName = movie.getMovieName();
        Optional<Movie> retrieved = movieRepository.findByMovieName(movieName);
        if(retrieved.isPresent()) {
            throw new MovieNameArleadyExists("The name: " + movieName + " is arleady taken.");
        } else {
//            Set<Genre> genres = movie.getGenres().stream()
//                    .filter(g -> Genre.contains(g.toString()))
//                    .collect(Collectors.toSet());
//            movie.setGenres(genres);
            return movieRepository.save(movie);
        }
    }


//    private void toSchedule(ScheduleInfo scheduleInfo) {
//        HttpEntity<ScheduleInfo> entity = new HttpEntity<>(scheduleInfo);
//        restTemplate.exchange("http://schedule-service/schedule", HttpMethod.POST, entity, ScheduleInfo.class);
//        System.out.println("Notified.");
//    }

    public Movie patchMovie(Map<String, Object> updates, int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isPresent()) {
            updates.forEach((k, v) -> {
                Field field = ReflectionUtils.findRequiredField(Movie.class, k);
                ReflectionUtils.setField(field,movie.get(), v);
            });
            scheduleMovie(movie.get());
            return movieRepository.save(movie.get());
        } else {
            throw new NoMovieWithThatId("There is no movie with the ID of: " + id);
        }
    }

    private void scheduleMovie(Movie movie) {
        HttpEntity<Movie> entity = new HttpEntity<>(movie, createAuthHeaders("Novac", "test1234"));
        ResponseEntity<String> response = restTemplate.exchange("http://schedule-service/schedule/update", HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
    }

    public Movie getMovieById(int id) {
        Optional<Movie> retrieved = movieRepository.findById(id);
        if(retrieved.isPresent()) {
            return retrieved.get();
        } else {
            throw new NoMovieWithThatId("There is no movie with the ID of: " + id);
        }
    }

    public Movie getMovieByName(String movieName) {
        Optional<Movie> retrieved = movieRepository.findByMovieName(movieName);
        if(retrieved.isPresent()) {
            return retrieved.get();
        } else {
            throw new NoMovieWithThatName("There is no movie with the name: " + movieName);
        }
    }

    private HttpHeaders createAuthHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }
}
