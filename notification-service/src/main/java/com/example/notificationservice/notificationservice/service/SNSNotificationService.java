package com.example.notificationservice.notificationservice.service;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.example.notificationservice.notificationservice.NotificationServiceApplication;
import com.example.notificationservice.notificationservice.model.Mail;
import com.example.notificationservice.notificationservice.model.MovieDTO;
import com.example.notificationservice.notificationservice.model.MovieDTOScheduleInfoDTOWrapper;
import com.example.notificationservice.notificationservice.model.ScheduleInfoDTO;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;

@Service
@Primary
public class SNSNotificationService implements NotificationService {

    @Autowired
    private AmazonSQS amazonSQS;
    @Autowired
    private EmailService emailService;

    @Autowired
    private AmazonSNS snsClient;

    private ObjectMapper mapper = new ObjectMapper();

    private final String topicArn = "arn:aws:sns:us-east-1:606588034018:Movies";

    public void publishMessage(MovieDTOScheduleInfoDTOWrapper wrapper) throws IOException {

    }

    public void addSubscriber(String endpoint) {
        Topics.subscribeQueue(snsClient,amazonSQS,topicArn,endpoint);
        System.out.println("Subscription for endpoint: " + endpoint + " has been successful.");
    }


    @Override
    public void publishMessage(String message) {
        //SNS Notification
        PublishRequest publishRequest = new PublishRequest(topicArn, message);
        PublishResult publishResult = snsClient.publish(publishRequest);
        if(publishResult.getSdkHttpMetadata().getHttpStatusCode() == 200) {
            System.out.println("Notification sent.");
        } else {
            System.out.println("There has been a problem.");
        }
        //EMAIL NOTIFICATION
//        Mail mail = new Mail();
//        mail.setMailContent(message);
//        mail.setMailSubject("New Movie Release");
//        try {
//            emailService.sendEmail(mail);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
    }




}
