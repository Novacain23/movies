package com.example.notificationservice.notificationservice.service;


import com.example.notificationservice.notificationservice.model.Customer;
import com.example.notificationservice.notificationservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    public String registerCustomer(Customer customer) {
        customerRepository.save(customer);
        return "Customer registered.";
    }
}
