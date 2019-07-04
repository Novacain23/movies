package com.example.notificationservice.notificationservice.service;

import com.example.notificationservice.notificationservice.model.MovieDTOScheduleInfoDTOWrapper;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class MQNotificationService implements NotificationService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @Override
//    public void publishMessage(String message) {
//        rabbitTemplate.convertAndSend("queue","message-queue", message);
//        System.out.println("Message: " + message + " has been sent.");
//    }



    @Override
    public void publishMessage(String message) {

    }


}
