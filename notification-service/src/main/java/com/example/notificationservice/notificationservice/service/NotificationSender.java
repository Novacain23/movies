package com.example.notificationservice.notificationservice.service;


import com.example.notificationservice.notificationservice.model.Customer;
import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;

@Service
public interface NotificationSender extends Callable<String>{

    public void handleMessage(Customer customer, MovieScheduleInfoWrapper movieScheduleInfoWrapper);

    public void setCustomer(Customer customer);

    public void setWrapper(MovieScheduleInfoWrapper wrapper);

    @Override
    public String call();

}
