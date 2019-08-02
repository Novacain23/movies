package com.example.customerservice.customerservice.controller;


import com.example.customerservice.customerservice.model.Customer;
import com.example.customerservice.customerservice.model.CustomerPrincipal;
import com.example.customerservice.customerservice.model.Genre;
import com.example.customerservice.customerservice.model.Region;
import com.example.customerservice.customerservice.service.CustomerService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "/register",produces = "application/json", consumes = "application/json")
    public String registerCustomer(@RequestBody @Valid Customer customer) {
        return customerService.registerCustomer(customer);
    }

    @DeleteMapping(value="/unregister")
    public String unregisterCustomer() {
        CustomerPrincipal principal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customerService.unregisterCustomer(principal.getCustomerName());
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/test")
    public List<Customer> getCustomersByCriteria(@RequestParam Set<Genre> genres,
                                                 @RequestParam Set<Region> regions,
                                                 @RequestParam int year,
                                                 @RequestParam String director) {
        return customerService.findCustomersByQueryCriteria(genres, regions, year, director);
    }


    @GetMapping("/get")
    public List<Customer> findCustomerByCriteria(@RequestParam Set<Genre> genres,
                                               @RequestParam Set<Region> regions,
                                               @RequestParam int year,
                                               @RequestParam String director) {
        return customerService.findCustomersByCriteria(genres, regions, year, director);
    }

//    @GetMapping("/get/batch")
//    public void findCustomersByCriteriaBatch(@RequestParam Set<Genre> genres,
//                                                  @RequestParam Set<Region> regions,
//                                                  @RequestParam int year,
//                                                  @RequestParam String director) {
//         customerService.findCustomersByCriteriaByBatch(genres,regions,year,director);
//    }

    @PatchMapping(value="/patch",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer patchCustomer(@RequestBody Map<String, Object> updates) {
        return customerService.patchCustomer(updates);
    }
//
//    @GetMapping(value = "/lots")
//    public void createLotsOfCustomers() {
//        customerService.createCustomers(10000);
//    }
}
