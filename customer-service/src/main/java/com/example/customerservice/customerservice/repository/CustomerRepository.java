package com.example.customerservice.customerservice.repository;

import com.example.customerservice.customerservice.model.Customer;
import com.example.customerservice.customerservice.model.Genre;
import com.example.customerservice.customerservice.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface    CustomerRepository extends JpaRepository<Customer, Integer> {


    @Query("SELECT c from Customer c where c.customerName = :customerName")
    public Optional<Customer> findByName(String customerName);

    @Query("SELECT c.email from Customer c where c.isCorporate = 0")
    public List<String> getAllNormalCustomerEmails();

    //@Query("SELECT c from Customer c, Genres g INNER JOIN Customer ON g.id = c.id where c.genres = :gen")
    //public List<Customer> findAllWithConstraints(String gen);

    @Query("SELECT DISTINCT c from Customer c JOIN c.genres g JOIN c.regions r JOIN c.directors d WHERE g IN :genres OR g = :gen AND r IN :regions AND c.isCorporate = 0 AND c.minYear < :year AND c.maxYear > :year AND d in :director OR d = :all")
    public List<Customer> findNormalCustomersByGenresAndRegions(Set<Genre> genres, Set<Region> regions, int year, String director, Genre gen, String all);

}
