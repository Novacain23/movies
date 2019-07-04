package com.example.queueservice.queueservice.listener;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MQListener {


    @RabbitListener(queues = "novacqueue")
    public void receive(String message) {
        System.out.println(message);
    }

}
