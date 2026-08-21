package com.skillsphere.user.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillsphere.user.entity.RoleName;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleName> getAllRoles() {

        return Arrays.asList(RoleName.values());
    }
}