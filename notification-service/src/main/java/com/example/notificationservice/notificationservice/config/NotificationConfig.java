package com.example.notificationservice.notificationservice.config;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;

public class NotificationConfig {


    @Bean
    public Exchange getExchange() {
        return new TopicExchange("queue");
    }

    @Bean
    public AmazonSQS getSqsClient() {
        return AmazonSQSClientBuilder.standard().withRegion("us-east-1").build();
    }


}
