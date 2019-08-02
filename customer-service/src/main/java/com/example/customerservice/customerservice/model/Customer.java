package com.example.customerservice.customerservice.model;


import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Columns;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;




@Entity
@Table(name = "customer", schema = "customers")
public class Customer {
    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "The customer name cannot be empty.")
    @Column(name = "customer_name")
    private String customerName;

    @Email(message = "This is not a valid email address.")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "This phone number cannot be empty.")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @Column(name = "is_corporate")
    private boolean isCorporate;

    @Column(name = "min_year")
    private int minYear;

    @Column(name = "max_year")
    private int maxYear;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "directors", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "directors", nullable = false)
    private List<String> directors;

    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(name = "genres", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "genres", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Genre> genres;

    @ElementCollection(targetClass = Region.class)
    @CollectionTable(name = "regions", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "regions", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Region> regions;

    @ElementCollection(targetClass = ContactOption.class)
    @CollectionTable(name = "contact_options", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "contact_options", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<ContactOption> contactOptions;

    @ManyToMany(cascade = {CascadeType.REMOVE,CascadeType.MERGE,CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "customer_role", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    private Customer(int id, @NotEmpty String customerName, String email, String phoneNumber, String password,
                    boolean isCorporate, Set<Genre> genres, Set<Region> regions, Set<ContactOption> contactOptions
                    ,List<Role> roles, int minYear, int maxYear, List<String> directors) {
        this.id = id;
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isCorporate = isCorporate;
        this.genres = genres;
        this.regions = regions;
        this.minYear = minYear;
        this.maxYear = maxYear;
        this.directors = directors;
        this.contactOptions = contactOptions;
        this.roles = roles;
    }

    private Customer(Customer customer) {
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

    public static class Builder {
        private String customerName;
        private String email;
        private String phoneNumber;
        private String password;
        private boolean isCorporate;
        private Set<Genre> genres;
        private Set<Region> regions;
        private int minYear;
        private int maxYear;
        private List<String> directors;
        private Set<ContactOption> contactOptions;
        private List<Role> roles;

        public Builder(String customerName) {
            this.customerName = customerName;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }
        public Builder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }
        public Builder isCorporate(boolean isCorporate) {
            this.isCorporate = isCorporate;
            return this;
        }
        public Builder withGenres(Set<Genre> genres) {
            this.genres = genres;
            return this;
        }
        public Builder withRegions(Set<Region> regions) {
            this.regions = regions;
            return this;
        }
        public Builder withMinYear(int minYear) {
            this.minYear = minYear;
            return this;
        }
        public Builder withMaxYear(int maxYear) {
            this.maxYear = maxYear;
            return this;
        }
        public Builder withDirectors(List<String> directors) {
            this.directors = directors;
            return this;
        }
        public Builder withContactOptions(Set<ContactOption> contactOptions) {
            this.contactOptions = contactOptions;
            return this;
        }
        public Builder withRoles(List<Role> roles) {
            this.roles = roles;
            return this;
        }
        public Customer build() {
            Customer customer = new Customer();
            customer.customerName = this.customerName;
            customer.email = this.email;
            customer.password = this.password;
            customer.phoneNumber = this.phoneNumber;
            customer.isCorporate = this.isCorporate;
            customer.genres = this.genres;
            customer.regions = this.regions;
            customer.minYear = this.minYear;
            customer.maxYear = this.maxYear;
            customer.directors = this.directors;
            customer.contactOptions = this.contactOptions;
            customer.roles = this.roles;
            return customer;
        }
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

    public int getMinYear() {
        return minYear;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    public int getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
                isCorporate == customer.isCorporate &&
                minYear == customer.minYear &&
                maxYear == customer.maxYear &&
                Objects.equals(customerName, customer.customerName) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(phoneNumber, customer.phoneNumber) &&
                Objects.equals(password, customer.password) &&
                Objects.equals(directors, customer.directors) &&
                Objects.equals(genres, customer.genres) &&
                Objects.equals(regions, customer.regions) &&
                Objects.equals(contactOptions, customer.contactOptions) &&
                Objects.equals(roles, customer.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerName, email, phoneNumber, password, isCorporate, minYear, maxYear, directors, genres, regions, contactOptions, roles);
    }
}
