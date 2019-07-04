package com.example.notificationservice.notificationservice.controller;


import com.example.notificationservice.notificationservice.model.Customer;
import com.example.notificationservice.notificationservice.model.Mail;
import com.example.notificationservice.notificationservice.model.MovieDTO;
import com.example.notificationservice.notificationservice.model.MovieDTOScheduleInfoDTOWrapper;
import com.example.notificationservice.notificationservice.model.ScheduleInfoDTO;
import com.example.notificationservice.notificationservice.service.CustomerService;
import com.example.notificationservice.notificationservice.service.EmailService;
import com.example.notificationservice.notificationservice.service.NotificationService;
import com.example.notificationservice.notificationservice.service.SNSNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/submit")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CustomerService customerService;
//
//    @PostMapping
//    public void publish(@RequestBody String message) {
//         notificationService.publishMessage(message);
//    }

    @PostMapping("/entry")
    public String sendNotification(@RequestBody String message) {
        notificationService.publishMessage(message);
        emailService.prepareMail(message);
        System.out.println("got the message");
        return "Received.";
    }

    @PostMapping("/customer")
    public String registerCustomer(@RequestBody Customer customer) {
        return customerService.registerCustomer(customer);
    }

//    @GetMapping("/test")
//    public void sendEmail() throws MessagingException, UnsupportedEncodingException {
//        Mail mail = new Mail();
//        mail.setMailTo("novacstoica@gmail.com");
//        mail.setMailSubject("O mers");
//        mail.setMailContent("A mers trimiterea.");
//        emailService.sendEmail(mail);
//    }





}
