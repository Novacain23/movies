package com.example.notificationservice.notificationservice.service;


import com.example.notificationservice.notificationservice.model.Customer;
import com.example.notificationservice.notificationservice.model.Genre;
import com.example.notificationservice.notificationservice.model.Movie;
import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import com.example.notificationservice.notificationservice.model.Region;
import com.example.notificationservice.notificationservice.repository.NotificationRepository;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class MessageService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EmailService emailService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository messageRepository;
    @Autowired
    private SmsService smsService;

    private static Logger log = LoggerFactory.getLogger(MessageService.class);

    @Async("asyncExecutor")
    public void handleNotification(MovieScheduleInfoWrapper movieScheduleInfoWrapper, boolean isUpdate){
        String message = isUpdate ? templateMessage(movieScheduleInfoWrapper) : classicTemplateMessage(movieScheduleInfoWrapper);
        List<String> arnTopic = Arrays.asList("arn:aws:sns:us-east-1:606588034018:Movies");
        List<Customer> customers = getCustomers(movieScheduleInfoWrapper);
        if(!customers.isEmpty()){
            List<String> emails = new ArrayList<>();
            List<String> phoneNumbers = new ArrayList<>();
            customers.forEach(c -> {
                if(c.getContactOptions().contains("EMAIL")){
                    emails.add(c.getEmail());
                }
                if(c.getContactOptions().contains("SMS")){
                    phoneNumbers.add(c.getPhoneNumber());
                }
            });

            emailService.handleNotification(message, emails);
            smsService.handleNotification(message, phoneNumbers);
            notificationService.publishMessage(message, arnTopic);


        }
    }

    private String templateMessage(MovieScheduleInfoWrapper w) {
        return "There's been an update on the movie: " + w.getMovie().getMovieName() + ", being made by the director: " + w.getMovie().getDirector() + " " +
                "has a duration of " + w.getMovie().getDuration() + " and has the next genres: " + w.getMovie().getGenres() + ". " +
                "It will be released on " + w.getScheduleInfo().getReleaseDate().getTime().toString() + " until " + w.getScheduleInfo().getEndDate().getTime().toString() + " in the "
                +w.getScheduleInfo().getRegion() + " region.";
    }

    private static String classicTemplateMessage(MovieScheduleInfoWrapper w) {
        return "The movie: " + w.getMovie().getMovieName() + ", being made by the director: " + w.getMovie().getDirector() + " " +
                "has a duration of " + w.getMovie().getDuration() + " and has the next genres: " + w.getMovie().getGenres() + ". " +
                "It will be released on " + w.getScheduleInfo().getReleaseDate().getTime().toString() + " until " + w.getScheduleInfo().getEndDate().getTime().toString() + " in the "
                +w.getScheduleInfo().getRegion() + " region.";

    }

    private List<Customer> getCustomers(MovieScheduleInfoWrapper movieScheduleInfo) {
        String finalUri = getUriWithCriterias(movieScheduleInfo);
        HttpEntity entity = new HttpEntity<>(createHeaders("Novac","test1234"));
        ResponseEntity<List<Customer>> customers = restTemplate.exchange(finalUri, HttpMethod.GET,entity, new ParameterizedTypeReference<List<Customer>>(){});
        return customers.getBody();
    }

    private String getUriWithCriterias(MovieScheduleInfoWrapper movieScheduleInfo) {
        Set<Genre> genres = movieScheduleInfo.getMovie().getGenres();
        Set<Region> regions = movieScheduleInfo.getScheduleInfo().getRegion();
        int year = movieScheduleInfo.getMovie().getYear();
        String director = movieScheduleInfo.getMovie().getDirector();
        UriComponents builder = UriComponentsBuilder.fromHttpUrl("http://customer-service/customer/get")
                .queryParam("genres",genres)
                .queryParam("regions",regions)
                .queryParam("year",year)
                .queryParam("director",director).build();
        return builder.toUriString().replace("[","").replace("]","");
    }

    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }



}
