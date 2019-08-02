package com.example.moviesservice.moviesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MoviesServiceApplication {


	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplateBuilder().basicAuthentication("Novac","test1234").build();
	}

	@Bean
	@LoadBalanced
	public WebClient.Builder getWebClient() {
		return  WebClient.builder();
	}


	public static void main(String[] args) {
		SpringApplication.run(MoviesServiceApplication.class, args);
	}

}
