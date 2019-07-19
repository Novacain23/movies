package com.example.moviesservice.moviesservice.service;

import com.example.moviesservice.moviesservice.exception.NameArleadyTaken;
import com.example.moviesservice.moviesservice.model.Admin;
import com.example.moviesservice.moviesservice.model.AdminPrincipal;
import com.example.moviesservice.moviesservice.repository.AdminRepository;
import com.example.moviesservice.moviesservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Admin> admin = adminRepository.findByName(username);
        if (!admin.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        Admin getAdmin = admin.get();
        return new AdminPrincipal(getAdmin.getName(), getAdmin.getPassword(), getAdmin.getRoles());
    }

    public Admin addAdmin(Admin admin) {
        if(adminRepository.findByName(admin.getName()).isPresent()){
            throw new NameArleadyTaken("This name is arleady taken.");
        } else {
            admin.setPassword(encoder.encode(admin.getPassword()));
            admin.setRoles(Arrays.asList(roleRepository.findByRole("ADMIN")));
            return adminRepository.save(admin);

        }
    }
}