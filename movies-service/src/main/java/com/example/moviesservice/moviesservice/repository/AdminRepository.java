package com.example.moviesservice.moviesservice.repository;

import com.example.moviesservice.moviesservice.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    @Query("SELECT a from Admin a where a.name = :name")
    public Optional<Admin> findByName(String name);
}
