package com.example.notificationservice.notificationservice.config;


import com.example.notificationservice.notificationservice.model.ContactOption;
import com.example.notificationservice.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.notificationservice.service.CallableEmailNotificationSenderImpl;
import com.example.notificationservice.notificationservice.service.EmailNotificationSenderImpl;
import com.example.notificationservice.notificationservice.service.NotificationSender;
import com.example.notificationservice.notificationservice.service.SMSNotificationSenderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Configuration
public class SenderConfig {

    @Autowired
    private SMSNotificationSenderImpl smsNotificationSender;
    @Autowired
    private EmailNotificationSenderImpl emailNotificationSender;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private NotificationRepository notificationRepository;


    @Bean
    public Map<ContactOption, NotificationSender> mapSenderStrategy() {
        Map<ContactOption, NotificationSender> map = new HashMap<>();
        map.put(ContactOption.EMAIL, new CallableEmailNotificationSenderImpl(mailSender, notificationRepository));
        map.put(ContactOption.SMS, smsNotificationSender);
        return map;
    }
}
