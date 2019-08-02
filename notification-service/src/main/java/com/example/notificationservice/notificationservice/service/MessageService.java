package com.example.notificationservice.notificationservice.service;

import com.example.notificationservice.notificationservice.model.Customer;
import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import com.example.notificationservice.notificationservice.repository.NotificationRepository;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import java.nio.charset.Charset;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private NotificationRepository messageRepository;
    @Autowired
    private NotificationSenderResolver resolver;

    private static Logger log = LoggerFactory.getLogger(MessageService.class);


    public void handleNotification(MovieScheduleInfoWrapper movieScheduleInfoWrapper, boolean isUpdate) {
        long start = System.currentTimeMillis();
        List<Customer> customers = getCustomers(movieScheduleInfoWrapper);
        long end = System.currentTimeMillis();
        System.out.println("Timer -> " + (start-end));
        resolver.resolve(customers, movieScheduleInfoWrapper);
    }

    private List<Customer> getCustomers(MovieScheduleInfoWrapper movieScheduleInfo) {
        String finalUri = getUriWithCriterias(movieScheduleInfo);
        //HttpEntity entity = new HttpEntity<>(createBasicAuthHeaders("Novac","test1234"));
        ResponseEntity<List<Customer>> customers = restTemplate.exchange(finalUri, HttpMethod.GET,null, new ParameterizedTypeReference<List<Customer>>(){});
        return customers.getBody();
    }

    private String getUriWithCriterias(MovieScheduleInfoWrapper movieScheduleInfo) {
        UriComponents builder = UriComponentsBuilder.fromHttpUrl("http://customer-service/customer/get")
                .queryParam("genres",movieScheduleInfo.getMovie().getGenres())
                .queryParam("regions",movieScheduleInfo.getScheduleInfo().getRegion())
                .queryParam("year",movieScheduleInfo.getMovie().getYear())
                .queryParam("director",movieScheduleInfo.getMovie().getDirector()).build();
        return builder.toUriString().replace("[","").replace("]","");
    }

    private HttpHeaders createBasicAuthHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }



}
