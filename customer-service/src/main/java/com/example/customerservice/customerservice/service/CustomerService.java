package com.example.customerservice.customerservice.service;


import com.example.customerservice.customerservice.exceptions.CustomerNameArleadyTaken;
import com.example.customerservice.customerservice.model.Customer;
import com.example.customerservice.customerservice.model.Genre;
import com.example.customerservice.customerservice.model.Region;
import com.example.customerservice.customerservice.repository.CustomerRepository;
import com.example.customerservice.customerservice.repository.RoleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.orm.hibernate5.HibernateOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

//    @Autowired
//    private SessionFactory sessionFactory;

    private final String topicArn = "arn:aws:sns:us-east-1:606588034018:Movies";


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
                    .withContactOptions(customer.getContactOptions()).build();
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
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("novac");
        EntityManager em = emfactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
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
        TypedQuery<Customer> tq = em.createQuery(cq);
        return tq.getResultList();
    }

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
