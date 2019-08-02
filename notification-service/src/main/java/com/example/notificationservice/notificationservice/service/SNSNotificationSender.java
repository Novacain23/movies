package com.example.notificationservice.notificationservice.service;


import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.example.notificationservice.notificationservice.model.Customer;
import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import com.example.notificationservice.notificationservice.model.Notification;
import com.example.notificationservice.notificationservice.repository.NotificationRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class SNSNotificationSender {

    @Autowired
    private AmazonSQS amazonSQS;
    @Autowired
    private AmazonSNS snsClient;
    @Autowired
    private NotificationRepository notificationRepository;

    private static Logger log = LoggerFactory.getLogger(SNSNotificationSender.class);
    private final String topicArn = "arn:aws:sns:us-east-1:606588034018:Movies";

    public void handleMessage(MovieScheduleInfoWrapper movieScheduleInfoWrapper) {
        String message = templateMessage(movieScheduleInfoWrapper);
        if(hasSubscriptions(topicArn)){
            persistResultIfSuccessful(publishMessage(message),message);
        } else {
            log.info("The topic: " + topicArn + " has no subscriptions.");
        }
    }

    private PublishResult publishMessage(String message) {
        PublishRequest publishRequest = new PublishRequest(topicArn, message);
        return snsClient.publish(publishRequest);
    }

    private void persistResultIfSuccessful(PublishResult result, String message){
        if(result.getSdkHttpMetadata().getHttpStatusCode() == 200) {
            log.info("Notification sent on topic: " + topicArn);
            persistTransaction(topicArn, message);
        } else {
            log.error("There has been a problem sending notification to: " + topicArn);
        }
    }

    private String templateMessage(MovieScheduleInfoWrapper wrapper) {
        return "This is an SNS notification for the release of the movie: " + wrapper.getMovie().getMovieName() + ", being made by the director: " + wrapper.getMovie().getDirector() + " " +
                "has a duration of " + wrapper.getMovie().getDuration() + " and has the next genres: " + wrapper.getMovie().getGenres() + ". " +
                "It will be released on " + wrapper.getScheduleInfo().getReleaseDate().getTime().toString() + " until " + wrapper.getScheduleInfo().getEndDate().getTime().toString() +
                " in the " + wrapper.getScheduleInfo().getRegion() + " region.";
    }

    private boolean hasSubscriptions(String topicArn) {
        return !snsClient.listSubscriptionsByTopic(topicArn).getSubscriptions().isEmpty();
    }

    private void persistTransaction(String topicArn, String message) {
        Notification notification = new Notification(topicArn, message, Calendar.getInstance(), true);
        notificationRepository.save(notification);
        log.info("Notification sent on topic: " + topicArn + " has been persisted in the DB.");
    }

    private void addSubscriber(String endpoint, String topicArn) {
        Topics.subscribeQueue(snsClient,amazonSQS,topicArn,endpoint);
        System.out.println("Subscription for endpoint: " + endpoint + " has been successful.");
    }





}
