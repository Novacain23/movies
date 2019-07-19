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


    @Transactional
    public void saveAndSend(ScheduleInfo scheduleInfo) {
        if (!checkIfSent(scheduleInfo)) {
            Movie retrieved = retrieveMovieByName(scheduleInfo.getMovieName());
            scheduleInfo.setMovieId(retrieved.getId());
            scheduleRepository.save(scheduleInfo);
            MovieScheduleInfoWrapper movieScheduleInfoWrapper = new MovieScheduleInfoWrapper(scheduleInfo, retrieved);
            String url = "http://notification-service/submit/entry";
            send(movieScheduleInfoWrapper, url);
        } else {
            System.out.println("Schedule for this movie is arleady sent.");
        }
    }

    private Boolean checkIfSent(ScheduleInfo scheduleInfo){
        Optional<ScheduleInfo> retrieved = scheduleRepository.findByMovieName(scheduleInfo.getMovieName());
        return retrieved.isPresent() &&  retrieved.get().isSent();
    }

    private Movie retrieveMovieByName(String movieName) throws NoMovieWithThatName {
        HttpEntity entity = new HttpEntity(createAuthHeaders("Novac","test1234"));
        return restTemplate.exchange("http://movies-service/movie/name/?movieName=" + movieName,HttpMethod.GET, entity,Movie.class).getBody();
    }

    private void send(MovieScheduleInfoWrapper movieScheduleInfoWrapper, String url) {
            HttpEntity<MovieScheduleInfoWrapper> entity = new HttpEntity<>(movieScheduleInfoWrapper, createAuthHeaders("Novac", "test1234"));
            ResponseEntity<String> returnMessage = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (returnMessage.getStatusCode().is2xxSuccessful()) {
                if(!movieScheduleInfoWrapper.getScheduleInfo().isSent()) {
                    scheduleRepository.setToSent(movieScheduleInfoWrapper.getMovie().getId());
                }
            } else {
                System.out.println("problem occured.");
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

    public String sendUpdatedMovie(Movie movie) {
        Optional<ScheduleInfo> movieSchedule = scheduleRepository.findById(movie.getId());

        if(movieSchedule.isPresent()){
            MovieScheduleInfoWrapper movieScheduleInfoWrapper = new MovieScheduleInfoWrapper(movieSchedule.get(),movie);
            String url = "http://notification-service/submit/entry/update";
            send(movieScheduleInfoWrapper,url);
            return "Sent updated movie.";
        } else {
            System.out.println("There is no schedule made for this updated movie.");
            return "Update received but hasn't been sent.";
        }
    }
}
