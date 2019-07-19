package com.example.customerservice.customerservice.service;

import com.example.customerservice.customerservice.model.Customer;
import com.example.customerservice.customerservice.model.CustomerPrincipal;
import com.example.customerservice.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Customer> customer = customerRepository.findByName(username);
        if (!customer.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        Customer getCustomer = customer.get();
        return new CustomerPrincipal(getCustomer.getCustomerName(), getCustomer.getPassword(), getCustomer.getRoles());
    }
}