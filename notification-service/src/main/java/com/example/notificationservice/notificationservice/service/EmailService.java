package com.example.notificationservice.notificationservice.service;

import com.example.notificationservice.notificationservice.model.Genre;
import com.example.notificationservice.notificationservice.model.Mail;
import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import com.example.notificationservice.notificationservice.model.Notification;
import com.example.notificationservice.notificationservice.model.Region;
import com.example.notificationservice.notificationservice.repository.NotificationRepository;
import com.sun.jersey.api.uri.UriComponent;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("mailService")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private NotificationRepository notificationRepository;

    @Async
    public void handleNotification(String message, List<String> emails) {
        if(emails.isEmpty()) {
            System.out.println("There is no subscriber interested in these genres or is in the appropriate region.");
        } else {
            prepareMail(message, emails);
        }
    }

    private String sendEmail(Mail mail, InternetAddress[] addresses) throws UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            mimeMessage.setRecipients(Message.RecipientType.TO, addresses);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress("testsoftvision10@gmail.com", "Softvisioner"));
            mimeMessageHelper.setText(mail.getMailContent(), true);
            mailSender.send(mimeMessageHelper.getMimeMessage());
            persistTransactions(mimeMessage, mail.getMailContent());
            return "Email sent.";
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "Email problem";
    }

    @Transactional
    private void persistTransactions(MimeMessage mimeMessage, String message) throws MessagingException {
        Arrays.stream(mimeMessage.getAllRecipients()).forEach(e -> {
                Notification notification = new Notification(e.toString(),message,Calendar.getInstance(),false);
                notificationRepository.save(notification);
        });

    }

    private String prepareMail(String message, List<String> emails){
        Mail mail = new Mail();
        mail.setMailSubject("A new movie release.");
        mail.setMailContent(message);
        InternetAddress[] addresses = convertToInternetAddresses(emails);
        try {
            return sendEmail(mail, addresses);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Problem.";
    }

    private InternetAddress[] convertToInternetAddresses(List<String> emails) {
        InternetAddress[] addresses = new InternetAddress[emails.size()];
        int count = 0;
        for(String s : emails) {
            try {
                 addresses[count] = new InternetAddress(s);
                 count++;
            } catch(AddressException e) {
                e.printStackTrace();
            }
        }
        return addresses;
    }

    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

//    private InternetAddress[] getInternetAddresses() {
//        List<String> emailAddresses = restTemplate.exchange("http://customer-service/customer/email/all", HttpMethod.GET,new HttpEntity<>(createHeaders("Novac","test1234")), new ParameterizedTypeReference<List<String>>() {
//        }).getBody();
//
//        //List<Customer> nonCorporateCustomers = customerRepository.findAllNonCorporateCustomers();
//            InternetAddress[] addresses = new InternetAddress[emailAddresses.size()];
//            int count = 0;
//            for (String s : emailAddresses) {
//                try {
//                    addresses[count] = new InternetAddress(s);
//                    count++;
//                } catch (AddressException e) {
//                    e.printStackTrace();
//                }
//            }
//            return addresses;
//
//    }


}