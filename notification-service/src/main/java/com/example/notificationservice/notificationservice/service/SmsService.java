package com.example.notificationservice.notificationservice.service;


import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import com.example.notificationservice.notificationservice.model.Notification;
import com.example.notificationservice.notificationservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

@Service
public class SmsService {

    @Autowired
    private NotificationRepository notificationRepository;


    @Async
    public void handleNotification(String message , List<String> phoneNumbers) {

        if(phoneNumbers.isEmpty()) {
            System.out.println("There is no recipient for sms notification.");
        } else {
            System.out.println(message);
            saveTransactions(message, phoneNumbers);
        }
    }

    @Transactional
    public void saveTransactions(String message, List<String> phoneNumbers){
        phoneNumbers.forEach(p -> {
            Notification notification = new Notification(p, message, Calendar.getInstance(), false);
            notificationRepository.save(notification);
        });
    }
}
