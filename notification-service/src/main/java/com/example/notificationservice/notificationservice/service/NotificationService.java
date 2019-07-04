package com.example.notificationservice.notificationservice.service;


import com.example.notificationservice.notificationservice.model.MovieDTOScheduleInfoDTOWrapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface NotificationService {

    public void publishMessage(String message);


}
