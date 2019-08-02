package com.example.notificationservice.notificationservice.model;


import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class Customer {

    private int id;
    private String customerName;
    private String email;
    private String phoneNumber;
    private String password;
    private boolean isCorporate;
    private Set<Genre> genres;
    private Set<Region> regions;
    private Set<ContactOption> contactOptions;
    private List<Role> roles;

    public Customer(int id,String customerName, String email, String phoneNumber, String password,
                    boolean isCorporate, Set<Genre> genres, Set<Region> regions, Set<ContactOption> contactOptions , List<Role> roles) {
        this.id = id;
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isCorporate = isCorporate;
        this.genres = genres;
        this.regions = regions;
        this.contactOptions = contactOptions;
        this.roles = roles;
    }

    public Customer(Customer customer) {
        this.customerName = customer.getCustomerName();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
        this.password = customer.getPassword();
        this.isCorporate = customer.isCorporate();
        this.contactOptions = customer.getContactOptions();
        this.roles = customer.getRoles();

    }

    public Customer() {
    }


    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isCorporate() {
        return isCorporate;
    }

    public void setCorporate(boolean corporate) {
        isCorporate = corporate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Region> getRegions() {
        return regions;
    }

    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }

    public Set<ContactOption> getContactOptions() {
        return contactOptions;
    }

    public void setContactOptions(Set<ContactOption> contactOptions) {
        this.contactOptions = contactOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
                isCorporate == customer.isCorporate &&
                Objects.equals(customerName, customer.customerName) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(phoneNumber, customer.phoneNumber) &&
                Objects.equals(password, customer.password) &&
                Objects.equals(genres, customer.genres) &&
                Objects.equals(regions, customer.regions) &&
                Objects.equals(contactOptions, customer.contactOptions) &&
                Objects.equals(roles, customer.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerName, email, phoneNumber, password, isCorporate, genres, regions, contactOptions, roles);
    }
}
