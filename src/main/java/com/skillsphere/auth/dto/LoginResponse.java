package com.skillsphere.auth.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles;
}