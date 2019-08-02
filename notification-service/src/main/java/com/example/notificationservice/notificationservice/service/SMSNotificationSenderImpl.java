package com.example.notificationservice.notificationservice.service;


import com.example.notificationservice.notificationservice.model.Customer;
import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import com.example.notificationservice.notificationservice.model.Notification;
import com.example.notificationservice.notificationservice.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

@Service
public class SMSNotificationSenderImpl implements NotificationSender, Callable<String> {

    @Autowired
    private NotificationRepository notificationRepository;

    private static Logger log = LoggerFactory.getLogger(SMSNotificationSenderImpl.class);

    @Override
    public void handleMessage(Customer customer, MovieScheduleInfoWrapper movieScheduleInfoWrapper) {
        String phoneNumber = customer.getPhoneNumber();
        String message = templateMessage(movieScheduleInfoWrapper);
        sendSMS(phoneNumber, message);
    }

    @Override
    public void setCustomer(Customer customer) {

    }

    @Override
    public void setWrapper(MovieScheduleInfoWrapper wrapper) {

    }

    private String templateMessage(MovieScheduleInfoWrapper wrapper) {
        return "This is an SMS for the release of the movie: " + wrapper.getMovie().getMovieName() + ", being made by the director: " + wrapper.getMovie().getDirector() + " " +
                "has a duration of " + wrapper.getMovie().getDuration() + " and has the next genres: " + wrapper.getMovie().getGenres() + ". " +
                "It will be released on " + wrapper.getScheduleInfo().getReleaseDate().getTime().toString() + " until " + wrapper.getScheduleInfo().getEndDate().getTime().toString() +
                " in the " + wrapper.getScheduleInfo().getRegion() + " region.";
    }

    private void sendSMS(String phoneNumber, String message) {
        log.info("SMS to " + phoneNumber + " with the message: " + message + " has been sent.");
        saveTransactions(message,phoneNumber);
    }

    @Transactional
    private void saveTransactions(String message, String phoneNumber){
            Notification notification = new Notification(phoneNumber, message, Calendar.getInstance(), false);
            notificationRepository.save(notification);
    }


    @Override
    public String call() {
        return null;
    }
}
