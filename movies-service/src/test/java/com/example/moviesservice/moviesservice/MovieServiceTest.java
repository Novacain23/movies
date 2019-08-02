package com.example.moviesservice.moviesservice;


import com.example.moviesservice.moviesservice.exception.MovieNameArleadyExists;
import com.example.moviesservice.moviesservice.exception.NoMovieWithThatId;
import com.example.moviesservice.moviesservice.exception.NoMovieWithThatName;
import com.example.moviesservice.moviesservice.model.Genre;
import com.example.moviesservice.moviesservice.model.Movie;
import com.example.moviesservice.moviesservice.repository.MovieRepository;
import com.example.moviesservice.moviesservice.service.MovieService;
import com.google.inject.matcher.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private MovieService movieService;

    Movie movie;
    Set<Genre> genres;
    @Before
    public void init() {
        genres = new HashSet<>();
        genres.add(Genre.ACTION);
        movie = new Movie();
        movie.setId(1);
        movie.setDirector("Nolan");
        movie.setMovieName("Inception");
        movie.setGenres(genres);
        movie.setYear(2000);
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void addMovie_ifMovieWithThatNameDoesntExist_saveMovie() {

        when(movieRepository.findByMovieName("Inception")).thenReturn(Optional.empty());
        when(movieRepository.save(movie)).thenReturn(movie);

        Movie returnedMovie = movieService.addMovie(movie);

        assertThat(returnedMovie.equals(movie));
        verify(movieRepository).save(movie);

    }

    @Test(expected = MovieNameArleadyExists.class)
    public void addMovie_ifMovieWithThatNameExists_throwException() {
        when(movieRepository.findByMovieName("Inception")).thenReturn(Optional.of(movie));
        movieService.addMovie(movie);
    }

    @Test
    public void getMovieById_ifMovieWithThatIdExists_returnMovie() {
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));

        Movie returnedMovie = movieService.getMovieById(1);

        assertThat(returnedMovie.equals(movie));
    }

    @Test(expected = NoMovieWithThatId.class)
    public void getMovieById_ifMovieWithThatIdDoesntExist_throwException() {
        when(movieRepository.findById(1)).thenReturn(Optional.empty());

        movieService.getMovieById(1);
    }

    @Test
    public void getMovieByName_ifMovieWithThatNameExists_returnMovie() {
        when(movieRepository.findByMovieName("Inception")).thenReturn(Optional.of(movie));

        Movie returnedMovie = movieService.getMovieByName("Inception");

        assertThat(returnedMovie.equals(movie));
    }

    @Test(expected = NoMovieWithThatName.class)
    public void getMovieByName_ifMovieWithThatNameDoesntExist_throwException() {
        when(movieRepository.findByMovieName("Inception")).thenReturn(Optional.empty());

        movieService.getMovieByName("Inception");
    }

    @Test(expected = NoMovieWithThatId.class)
    public void patchMovie_ifMovieWithThatIdDoesntExist_throwException() {
        when(movieRepository.findById(1)).thenReturn(Optional.empty());
        Map<String, Object> updates = new HashMap<>();
        movieService.patchMovie(updates,1);
    }

    @Test
    public void patchMovie_ifMovieWithThatIdExists_returnPatchedMovie() {
        //SET UP
        Map<String, Object> updates = new HashMap<>();
        updates.put("movieName","Pulp Fiction");
        ResponseEntity<String> response = new ResponseEntity<>("ok", HttpStatus.OK);
        Movie updatedMovie = movie;
        updatedMovie.setMovieName("Pulp Fiction");
        //WHEN
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));
        when(restTemplate.exchange(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<Movie>>any(),
                ArgumentMatchers.<Class<String>>any())).thenReturn(response);

        when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);
        Movie returnedMovie = movieService.patchMovie(updates,1);

        //THEN
        assertThat(returnedMovie.getMovieName().equals("Pulp Fiction"));
        verify(movieRepository).save(updatedMovie);
    }

    @Test
    public void patchMovie_ifMovieWithThatIdExistsAndGenresArePatched_returnPatchedMovie() {
        //SET UP
        Map<String, Object> updates = new HashMap<>();
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.DRAMA);
        updates.put("genres",genres);
        ResponseEntity<String> response = new ResponseEntity<>("ok", HttpStatus.OK);
        Movie updatedMovie = movie;
        updatedMovie.setGenres(genres);
        //WHEN
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));
        when(restTemplate.exchange(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<Movie>>any(),
                ArgumentMatchers.<Class<String>>any())).thenReturn(response);

        when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);
        Movie returnedMovie = movieService.patchMovie(updates,1);

        //THEN
        assertThat(returnedMovie.getMovieName().equals("Pulp Fiction"));
        verify(movieRepository).save(updatedMovie);
    }


}
