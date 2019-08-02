package com.example.customerservice.customerservice.service;


import com.example.customerservice.customerservice.exceptions.CustomerNameArleadyTaken;
import com.example.customerservice.customerservice.model.ContactOption;
import com.example.customerservice.customerservice.model.Customer;
import com.example.customerservice.customerservice.model.Genre;
import com.example.customerservice.customerservice.model.Region;
import com.example.customerservice.customerservice.model.Role;
import com.example.customerservice.customerservice.repository.CustomerRepository;
import com.example.customerservice.customerservice.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EntityManager entityManager;


    private static Logger log = LoggerFactory.getLogger(CustomerService.class);


//    @Autowired
//    private SessionFactory sessionFactory;

    private final String topicArn = "arn:aws:sns:us-east-1:606588034018:Movies";

//    public void createCustomers(int nr) {
//        Set<Region> regions = new HashSet<>();
//        regions.add(Region.EUNE);
//        Set<Genre> genres = new HashSet<>();
//        genres.add(Genre.ACTION);
//        Set<ContactOption> contactOptions = new HashSet<>();
//        contactOptions.add(ContactOption.EMAIL);
//        Customer customer = new Customer.Builder("StressTest")
//                .withPassword("test1234")
//                .withEmail("test@yahoo.com")
//                .withPhoneNumber("0720461217")
//                .withDirectors(Arrays.asList("Nolan"))
//                .withRegions(regions)
//                .withGenres(genres)
//                .withMinYear(1970)
//                .withContactOptions(contactOptions)
//                .withMaxYear(2000).build();
//        for (int start = 0; start < nr; start++) {
//            customer.setCustomerName("StressTest" + start);
//            registerCustomer(customer);
//        }
//    }

    @Transactional
    public String registerCustomer(Customer customer) {
        if(customerRepository.findByName(customer.getCustomerName()).isPresent()) {
            throw new CustomerNameArleadyTaken("This name is arleady taken. Please pick something else.");
        } else {
            Set<Genre> defaultGenres = new HashSet<>();
            defaultGenres.add(Genre.ALL);
            Customer create = new Customer.Builder(customer.getCustomerName())
                    .withEmail(customer.getEmail())
                    .withPhoneNumber(customer.getPhoneNumber())
                    .withGenres(customer.getGenres() == null ? defaultGenres : customer.getGenres())
                    .withRegions(customer.getRegions())
                    .withMinYear(customer.getMinYear())
                    .withMaxYear(customer.getMaxYear() == 0 ? 5000 : customer.getMaxYear())
                    .withDirectors(customer.getDirectors() == null ? Arrays.asList("ALL") : customer.getDirectors())
                    .withContactOptions(customer.getContactOptions())
                    .withRoles(Arrays.asList(new Role("USER"))).build();
            create.setPassword(encoder.encode(customer.getPassword()));
            create.setRoles(Arrays.asList(roleRepository.findByRole("USER")));
            customerRepository.save(create);
            return handleRegistration(create);
        }
    }

    /** Find all the customers that correspond to the criteria given. Based on a Hibernate Criteria.
     *
     * @param genres
     * @param regions
     * @param year
     * @param director
     * @return List<Customer>
     */

    public List<Customer> findCustomersByCriteria(Set<Genre> genres, Set<Region> regions, int year, String director) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> customer = cq.from(Customer.class);
        Path<Map<String, String>> genrePath = customer.join("genres");
        Path<Map<String,String>> regionPath = customer.join("regions");
        Path<Map<String, String>> directorPath = customer.join("directors");
        Predicate predicate = cb.and(
                cb.or(genrePath.in(genres),genrePath.in(Genre.ALL)),
                cb.or(directorPath.in(director), directorPath.in("ALL")),
                        cb.isTrue(regionPath.in(regions)),
                        cb.lessThanOrEqualTo(customer.get("minYear"),year),
                        cb.greaterThanOrEqualTo(customer.get("maxYear"),year));
        cq.select(customer).where(predicate);
        TypedQuery<Customer> tq = entityManager.createQuery(cq);
        List<Customer> customers = tq.getResultList();
        return customers;
    }

//    public void findCustomersByCriteriaByBatch(Set<Genre> genres, Set<Region> regions, int year, String director) {
//        Connection connection = null;
//        PreparedStatement statement = null;
//        try {
//           Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
//           connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/customers","root","Softvision10");
//           statement = connection.prepareStatement("SELECT DISTINCT  from Customer c JOIN c.genres g JOIN c.regions r JOIN c.directors d " +
//                   "WHERE g IN :genres OR g = :gen AND r IN :regions AND c.isCorporate = 0 " +
//                   "AND c.minYear < :year AND c.maxYear > :year AND d in :director OR d = :all");
//           statement.setInt(1,15);
//
//           statement.setFetchSize(Integer.MIN_VALUE);
//           ResultSet rs = statement.executeQuery();
//
//           while(rs.next()){
//               Customer customer = (Customer) rs;
//               System.out.println(customer);
//           }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


    /** Find all the customers that correspond to the criteria given. Based on an SQL Query.
     *
     * ##### NOT USED ATM #####
     *
     * @param genres
     * @param regions
     * @param year
     * @param director
     * @return List<Customer>
     */
    public List<Customer> findCustomersByQueryCriteria(Set<Genre> genres, Set<Region> regions, int year, String director) {
        String all = "ALL";
        return customerRepository.findNormalCustomersByGenresAndRegions(genres,regions,year,director, Genre.ALL, all);
    }

    private String handleRegistration(Customer customer) {

        if(customer.isCorporate()) {
            return "Thank you for joining. Please subscribe to the topic: " + topicArn + " to received updates.";
        } else {
            return "Thank you for joining. We will email/SMS you any movie updates.";
        }
    }

    public String unregisterCustomer(String customerName) {
        customerRepository.delete(customerRepository.findByName(customerName).get());
        return "Deleted.";
    }


    public Customer patchCustomer(Map<String, Object> updates) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByName(name).get();
            updates.forEach((k, v) -> {
                if(k.equals("genres") || k.equals("regions")) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<HashSet<Genre>>(){}.getType();
                    v = gson.fromJson(v.toString(), type);
                }
                    Field field = ReflectionUtils.findRequiredField(Customer.class, k);
                    ReflectionUtils.setField(field, customer, v);

            });
           // return customerRepository.save(customer);
        return customerRepository.save(customer);

    }



}
