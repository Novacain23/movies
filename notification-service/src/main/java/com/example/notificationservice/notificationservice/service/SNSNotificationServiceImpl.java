package com.example.notificationservice.notificationservice.service;


import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import com.example.notificationservice.notificationservice.model.Notification;
import com.example.notificationservice.notificationservice.repository.NotificationRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
@Primary
public class SNSNotificationServiceImpl implements NotificationService {

    @Autowired
    private AmazonSQS amazonSQS;
    @Autowired
    private AmazonSNS snsClient;
    @Autowired
    private NotificationRepository notificationRepository;

    private ObjectMapper mapper = new ObjectMapper();


    public void addSubscriber(String endpoint, String topicArn) {
        Topics.subscribeQueue(snsClient,amazonSQS,topicArn,endpoint);
        System.out.println("Subscription for endpoint: " + endpoint + " has been successful.");
    }


    public void publishMessage(String message, List<String> recipients) {
        recipients.forEach(topicArn -> {
            if (hasSubscriptions(topicArn)) {
                PublishRequest publishRequest = new PublishRequest(topicArn, message);
                PublishResult publishResult = snsClient.publish(publishRequest);
                if (publishResult.getSdkHttpMetadata().getHttpStatusCode() == 200) {
                    Notification notification = new Notification(topicArn, message, Calendar.getInstance(), true);
                    notificationRepository.save(notification);
                    System.out.println("Notification sent.");
                } else {
                    System.out.println("There has been a problem.");
                }
            } else{
                System.out.println("The topic: " + topicArn + " has no subscriptions.");
            }
        });

    }

    private boolean hasSubscriptions(String topicArn) {
        return !snsClient.listSubscriptionsByTopic(topicArn).getSubscriptions().isEmpty();
    }


}
