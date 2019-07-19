package com.example.customerservice.customerservice;

import com.example.customerservice.customerservice.model.Customer;
import com.example.customerservice.customerservice.model.Genre;
import com.example.customerservice.customerservice.model.Region;
import com.example.customerservice.customerservice.model.Role;
import com.example.customerservice.customerservice.repository.CustomerRepository;
import com.example.customerservice.customerservice.repository.RoleRepository;
import com.example.customerservice.customerservice.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CustomerService customerService;


    Role role = new Role();

    @Before
    private void init() {
        role.setRole("USER");
        Set<Genre> genres = new HashSet<Genre>();
        genres.add(Genre.ACTION);
        Set<Region> regions = new HashSet<Region>();
        regions.add(Region.EUNE);
        Customer customer = new Customer.Builder("Novac")
                .withEmail("novacstoica@gmail.com")
                .withPhoneNumber("0720461217")
                .withGenres(genres)
                .withRegions(regions)
                .withMinYear(1970)
                .withMaxYear(2000)
                .withDirectors(Arrays.asList("ALL"))
                .withContactOptions(Arrays.asList("EMAIL")).build();
        customer.setPassword(passwordEncoder.encode("test1234"));
        customer.setRoles(Arrays.asList(role));
        MockitoAnnotations.initMocks(this);
    }


}
