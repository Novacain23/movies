package com.example.scheduleservice.scheduleservice.service;


import com.example.scheduleservice.scheduleservice.model.Movie;
import com.example.scheduleservice.scheduleservice.model.MovieDTO;
import com.example.scheduleservice.scheduleservice.model.MovieDTOScheduleInfoDTOWrapper;
import com.example.scheduleservice.scheduleservice.model.MovieScheduleInfoWrapper;
import com.example.scheduleservice.scheduleservice.model.ScheduleInfo;
import com.example.scheduleservice.scheduleservice.model.ScheduleInfoDTO;
import com.example.scheduleservice.scheduleservice.repository.ScheduleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private RestTemplate restTemplate;

    private ModelMapper modelMapper = new ModelMapper();


    public void schedule(ScheduleInfo scheduleInfo) {
        scheduleRepository.save(scheduleInfo);
        System.out.println("Sent to repo.");
    }

    public Movie retrieveMovie(int id) {
        ResponseEntity<Movie> retrievedMovie = restTemplate.exchange("http://movies-service/movie/" + id,HttpMethod.GET, null, Movie.class);
        return retrievedMovie.getBody();
    }

    public List<MovieScheduleInfoWrapper> getCloseToReleaseScheduleInfoAndMovieList() {
        List<MovieScheduleInfoWrapper> list = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        scheduleRepository.findAll().stream()
                .filter(s -> !s.isSent() && ChronoUnit.DAYS.between(s.getReleaseDate().toInstant(),now.toInstant()) == 0 && s.getReleaseDate().after(now))
                .forEach(s -> list.add(new MovieScheduleInfoWrapper(s, retrieveMovie(s.getMovieId()))));

        return list;
    }


    public List<MovieDTOScheduleInfoDTOWrapper> mapMovieAndScheduleInfoToDTO(List<MovieScheduleInfoWrapper> list) {
        List<MovieDTOScheduleInfoDTOWrapper> entityContent = new ArrayList<>();
            list.forEach(e -> {
            //entity.put(modelMapper.map(s, ScheduleInfoDTO.class),modelMapper.map(m,MovieDTO.class));
                MovieDTO movieDTO = modelMapper.map(e.getMovie(), MovieDTO.class);
                ScheduleInfoDTO scheduleInfoDTO = modelMapper.map(e.getScheduleInfo(), ScheduleInfoDTO.class);
                entityContent.add(new MovieDTOScheduleInfoDTOWrapper(movieDTO, scheduleInfoDTO));
        } );
            return entityContent;
    }

    public String sendEntity(HttpEntity<String> entity) {
        ResponseEntity<String> returnMessage = restTemplate.exchange("http://notification-service/submit/entry", HttpMethod.POST, entity, String.class);
        return returnMessage.getBody();
    }

    @Transactional
    public void setToSent(List<Integer> ids) {
        ids.stream()
                .forEach(i -> {
                    scheduleRepository.setToSent(i);
                });
    }

    public String templateMessage(MovieScheduleInfoWrapper w) {
        return "The movie: " + w.getMovie().getMovieName() + ", being made by the director: " + w.getMovie().getDirector() + " " +
                "has a duration of " + w.getMovie().getDuration() + " and has the next genres: " + w.getMovie().getGenres() + ". " +
                "It will be released on " + w.getScheduleInfo().getReleaseDate().getTime().toString() + " until " + w.getScheduleInfo().getEndDate().getTime().toString() + " in the "
                +w.getScheduleInfo().getRegion() + " region.";

    }
}
