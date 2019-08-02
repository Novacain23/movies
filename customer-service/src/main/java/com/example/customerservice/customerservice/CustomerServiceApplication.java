package com.example.customerservice.customerservice;

import com.example.customerservice.customerservice.model.Customer;
import com.example.customerservice.customerservice.model.Genre;
import com.example.customerservice.customerservice.model.Region;
import com.example.customerservice.customerservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableDiscoveryClient
public class CustomerServiceApplication {

	@Autowired
	private CustomerService customerService;

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);

	}

}
