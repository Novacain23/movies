package com.example.scheduleservice.scheduleservice.controller;


import com.example.scheduleservice.scheduleservice.model.Admin;
import com.example.scheduleservice.scheduleservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    //@PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(value = "/new",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Admin addAdmin(@RequestBody @Valid Admin admin) {
        return adminService.addAdmin(admin);
    }
}
