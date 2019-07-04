package com.example.notificationservice.notificationservice.repository;


import com.example.notificationservice.notificationservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {


    @Query("SELECT c from Customer c where c.isCorporate = 0")
    public List<Customer> findAllNonCorporateCustomers();
}
