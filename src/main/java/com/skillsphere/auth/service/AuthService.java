package com.skillsphere.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillsphere.audit.service.AuditService;
import com.skillsphere.auth.dto.LoginRequest;
import com.skillsphere.auth.dto.LoginResponse;
import com.skillsphere.auth.dto.RegisterRequest;
import com.skillsphere.auth.dto.RegisterResponse;
import com.skillsphere.exception.DuplicateResourceException;
import com.skillsphere.exception.InvalidCredentialsException;
import com.skillsphere.exception.ResourceNotFoundException;
import com.skillsphere.security.JwtService;
import com.skillsphere.user.entity.Role;
import com.skillsphere.user.entity.RoleName;
import com.skillsphere.user.entity.User;
import com.skillsphere.user.repository.RoleRepository;
import com.skillsphere.user.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuditService auditService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            AuditService auditService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.auditService = auditService;
    }
    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        // 1. Check whether email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "User already exists with email: " + request.getEmail()
            );
        }

        // 2. Find default STUDENT role
        Role studentRole = roleRepository.findByName(RoleName.STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Default STUDENT role not found"
                ));

        // 3. Create user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .active(true)
                .build();

        // 4. Assign STUDENT role
        user.getRoles().add(studentRole);

     // 5. Save user
        User savedUser = userRepository.save(user);

        // 6. Create audit log
        auditService.log(
                savedUser.getId(),
                "USER_REGISTERED",
                "User registered successfully"
        );

        // 7. Return safe response
        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                "User registered successfully"
        );
    }
    
    public LoginResponse login(LoginRequest request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (Exception exception) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Invalid email or password"
                ));

        String token = jwtService.generateToken(user);
        
        auditService.log(
                user.getId(),
                "USER_LOGIN",
                "User logged in successfully"
        );

        return new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles()
                        .stream()
                        .map(role -> role.getName().name())
                        .collect(java.util.stream.Collectors.toSet())
        );
    }
   
}