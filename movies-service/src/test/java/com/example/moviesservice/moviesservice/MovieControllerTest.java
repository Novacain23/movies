package com.example.moviesservice.moviesservice;

import com.example.moviesservice.moviesservice.controller.MovieController;
import com.example.moviesservice.moviesservice.model.Genre;
import com.example.moviesservice.moviesservice.model.Movie;
import com.example.moviesservice.moviesservice.service.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MoviesServiceApplication.class)
@AutoConfigureMockMvc
public class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController = new MovieController();

    private MockMvc mockMvc;
    private Movie movie;
    private ObjectMapper objectMapper = new ObjectMapper();






    @Before
    public void init() throws JsonProcessingException {
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.ACTION);
        movie = new Movie();
        movie.setId(1);
        movie.setMovieName("Inception");
        movie.setYear(2012);
        movie.setDuration(220);
        movie.setGenres(genres);
        movie.setDirector("Nolan");

        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    public void addMovie() throws Exception {
        when(movieService.addMovie(movie)).thenReturn(movie);
        String json = objectMapper.writeValueAsString(movie);
        this.mockMvc.perform(post("/movie/new")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieName").value("Inception"))
                .andExpect(status().isCreated());

    }

    @Test
    public void patchMovie() throws Exception {
        Map<String, Object> patchMap = new HashMap<>();
        patchMap.put("movieName","Interstellar");
        Movie modifiedMovie = new Movie();
        modifiedMovie.setMovieName("Interstellar");
        String json = objectMapper.writeValueAsString(patchMap);
        when(movieService.patchMovie(patchMap, 1)).thenReturn(modifiedMovie);

        this.mockMvc.perform(patch("/movie/1")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(json))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.movieName").value("Interstellar"))
                    .andExpect(status().isOk());
    }

    @Test
    public void getMovieById() throws Exception {
        when(movieService.getMovieById(1)).thenReturn(movie);

        this.mockMvc.perform(get("/movie/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieName").value("Inception"))
                .andExpect(status().isOk());
    }

    @Test
    public void getMovieByName() throws Exception {
        when(movieService.getMovieByName("Inception")).thenReturn(movie);

        this.mockMvc.perform(get("/movie/name")
                .param("movieName","Inception"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieName").value("Inception"))
                .andExpect(status().isOk());
    }




}
