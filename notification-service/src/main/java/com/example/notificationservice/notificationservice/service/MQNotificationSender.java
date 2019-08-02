package com.example.notificationservice.notificationservice.service;

import com.example.notificationservice.notificationservice.model.Customer;
import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;


@Service
public class MQNotificationSender implements NotificationSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @Override
//    public void handleMessage(String message) {
//        rabbitTemplate.convertAndSend("queue","message-queue", message);
//        System.out.println("Message: " + message + " has been sent.");
//    }

    @Override
    public void handleMessage(Customer customer, MovieScheduleInfoWrapper movieScheduleInfoWrapper) {

    }

    @Override
    public void setCustomer(Customer customer) {

    }

    @Override
    public void setWrapper(MovieScheduleInfoWrapper wrapper) {

    }

    @Override
    public String call() {
        return null;
    }
}
