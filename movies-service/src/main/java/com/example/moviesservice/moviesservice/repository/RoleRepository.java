package com.example.moviesservice.moviesservice.repository;

import com.example.moviesservice.moviesservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

   Role findByRole(String name);
}
