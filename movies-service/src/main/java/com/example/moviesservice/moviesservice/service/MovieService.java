package com.example.moviesservice.moviesservice.service;


import com.example.moviesservice.moviesservice.exception.MovieNameArleadyExists;
import com.example.moviesservice.moviesservice.exception.NoMovieWithThatId;
import com.example.moviesservice.moviesservice.exception.NoMovieWithThatName;
import com.example.moviesservice.moviesservice.model.Genre;
import com.example.moviesservice.moviesservice.model.Movie;
import com.example.moviesservice.moviesservice.repository.MovieRepository;
import com.google.gson.Gson;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashSet;
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


    /**
     * Create a new movie entity.
     * @param movie
     * @return
     */
    @Transactional
    @ResponseStatus(value = HttpStatus.CREATED)
    public Movie addMovie(Movie movie) {
        String name = movie.getMovieName();
        Optional<Movie> retrievedMovie = movieRepository.findByMovieName(name);
        if(retrievedMovie.isPresent()) {
            throw new MovieNameArleadyExists("The name: " + name + " is arleady taken.");
        } else {
            return movieRepository.save(movie);
        }
    }

    public Movie getMovieById(int id) {
        Optional<Movie> retrievedMovie = movieRepository.findById(id);
        if(retrievedMovie.isPresent()) {
            return retrievedMovie.get();
        } else {
            throw new NoMovieWithThatId("There is no movie with the ID of: " + id);
        }
    }

    public Movie getMovieByName(String movieName) {
        Optional<Movie> retrievedMovie = movieRepository.findByMovieName(movieName);
        if(retrievedMovie.isPresent()) {
            return retrievedMovie.get();
        } else {
            throw new NoMovieWithThatName("There is no movie with the name: " + movieName);
        }
    }

    public Movie patchMovie(Map<String, Object> updates, int id) {
        Optional<Movie> retrievedMovie = movieRepository.findById(id);
        if(retrievedMovie.isPresent()) {
            updates.forEach((k, v) -> {
                if(k.equals("genres")) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<HashSet<Genre>>(){}.getType();
                    v = gson.fromJson(v.toString(), type);
                }
                Field field = ReflectionUtils.findRequiredField(Movie.class, k);
                ReflectionUtils.setField(field,retrievedMovie.get(), v);
            });
            scheduleUpdateToMovie(retrievedMovie.get());
            return movieRepository.save(retrievedMovie.get());
        } else {
            throw new NoMovieWithThatId("There is no movie with the ID of: " + id);
        }
    }

    private void scheduleUpdateToMovie(Movie movie) {
        HttpEntity<Movie> entityToBeSent = new HttpEntity<>(movie);
        ResponseEntity<String> response = restTemplate.exchange("http://schedule-service/schedule/update", HttpMethod.POST, entityToBeSent, String.class);
        System.out.println(response.getBody());
    }
}
