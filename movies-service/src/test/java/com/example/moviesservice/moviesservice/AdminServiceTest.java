package com.example.moviesservice.moviesservice;

import com.example.moviesservice.moviesservice.exception.NameArleadyTaken;
import com.example.moviesservice.moviesservice.model.Admin;
import com.example.moviesservice.moviesservice.model.AdminPrincipal;
import com.example.moviesservice.moviesservice.model.Role;
import com.example.moviesservice.moviesservice.repository.AdminRepository;
import com.example.moviesservice.moviesservice.repository.RoleRepository;
import com.example.moviesservice.moviesservice.service.AdminService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringRunner.class)
public class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AdminService adminService;

    Admin admin;
    Role role;
    List<Role> roles;

    @Before
    public void init(){
        role = new Role();
        role.setRoleId(1);
        role.setRole("ADMIN");
        roles = new ArrayList<>();
        roles.add(role);
        admin = new Admin();
        admin.setName("Novac");
        admin.setPassword("test1234");
        admin.setId(1);
        MockitoAnnotations.initMocks(this);
    }


    @Test(expected = NameArleadyTaken.class)
    public void addAdmin_whenNameArleadyTaken_throwException() {
        when(adminRepository.findByName("Novac")).thenReturn(Optional.of(admin));

        adminService.addAdmin(admin);
    }

    @Test
    public void addAdmin_whenNameNotTaken_returnAdmin() {
        Admin changedAdmin = admin;
        changedAdmin.setPassword("dada");
        changedAdmin.setRoles(roles);

        when(adminRepository.findByName("Novac")).thenReturn(Optional.empty());
        when(encoder.encode(admin.getPassword())).thenReturn("dada");
        when(roleRepository.findByRole("ADMIN")).thenReturn(role);
        when(adminRepository.save(admin)).thenReturn(changedAdmin);

        Admin returnedAdmin = adminService.addAdmin(admin);

        assertThat(returnedAdmin.getPassword().equals("dada"));
        assertThat(returnedAdmin.getRoles().contains(role));
        verify(adminRepository).save(changedAdmin);


    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_whenUsernameIsNotFound_throwException() {
        when(adminRepository.findByName("Novac")).thenReturn(Optional.empty());

        adminService.loadUserByUsername("Novac");
    }

    @Test
    public void loadUserByUsername_whenUsernameIsFound_loadUser() {
        when(adminRepository.findByName("Novac")).thenReturn(Optional.of(admin));

        AdminPrincipal returned = (AdminPrincipal) adminService.loadUserByUsername("Novac");

        assertThat(returned.getName().equals("Novac"));
        assertThat(returned.getPassword().equals("test1234"));

    }
}
