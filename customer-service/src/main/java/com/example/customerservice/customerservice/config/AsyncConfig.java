package com.example.customerservice.customerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    @Bean(name = "executorService")
    public ExecutorService executorService() {
        return  Executors.newFixedThreadPool(10);
    }
}

