package com.example.scheduleservice.scheduleservice.repository;

import com.example.scheduleservice.scheduleservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

   Role findByRole(String name);
}
