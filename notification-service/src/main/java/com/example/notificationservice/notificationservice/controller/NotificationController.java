package com.example.notificationservice.notificationservice.controller;


import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import com.example.notificationservice.notificationservice.service.EmailNotificationSenderImpl;
import com.example.notificationservice.notificationservice.service.MessageService;
import com.example.notificationservice.notificationservice.service.NotificationSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/submit")
public class NotificationController {

    @Autowired
    private EmailNotificationSenderImpl emailNotificationServiceImpl;
    @Autowired
    private MessageService messageService;
//
//    @PostMapping
//    public void publish(@RequestBody String message) {
//         notificationSender.handleMessage(message);
//    }

    @PostMapping("/entry")
    public String receiveNotification(@RequestBody MovieScheduleInfoWrapper movieScheduleInfo) {
        messageService.handleNotification(movieScheduleInfo, false);
        System.out.println("got the message");
        return "Received.";
    }

    @PostMapping(value = "/entry/update", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public String receiveUpdateNotification(@RequestBody MovieScheduleInfoWrapper movieScheduleInfoWrapper) {
        messageService.handleNotification(movieScheduleInfoWrapper, true);
        System.out.println("got the update message");
        return "Received.";
    }



//    @GetMapping("/test")
//    public void sendEmail() throws MessagingException, UnsupportedEncodingException {
//        Mail mail = new Mail();
//        mail.setMailTo("novacstoica@gmail.com");
//        mail.setMailSubject("O mers");
//        mail.setMailContent("A mers trimiterea.");
//        emailNotificationServiceImpl.sendEmail(mail);
//    }





}
