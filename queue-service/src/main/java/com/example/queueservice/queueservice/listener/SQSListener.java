package com.example.queueservice.queueservice.listener;

import com.amazonaws.services.sqs.model.Message;
import com.example.queueservice.queueservice.model.SnsJson;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Component
public class SQSListener {

    @SqsListener("movie-sqs")
    public void getMessage(String message) {
        Gson gson = new Gson();
        SnsJson result = gson.fromJson(message, SnsJson.class);
        System.out.println(result.getMessage());


    }

}
