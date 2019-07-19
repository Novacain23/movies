package com.example.notificationservice.notificationservice.service;


import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {



    public void publishMessage(String message, List<String> recipients);


}
