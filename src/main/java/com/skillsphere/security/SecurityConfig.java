package com.skillsphere.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

            	    // Authentication APIs
            	    .requestMatchers("/api/auth/register").permitAll()
            	    .requestMatchers("/api/auth/login").permitAll()
            	    .requestMatchers("/api/auth/forgot-password").permitAll()
            	    .requestMatchers("/api/auth/reset-password").permitAll()

            	    // Web pages
            	    .requestMatchers(
            	    	    "/",
            	    	    "/register.html",
            	    	    "/login.html",
            	    	    "/dashboard.html",
            	    	    "/admin-dashboard.html",
            	    	    "/admin-users.html",
            	    	    "/role-management.html",
            	    	    "/audit-logs.html",
            	    	    "/reports.html",
            	    	    "/payment-management.html",
            	    	    "/certification-management.html",
            	    	    "/notification-center.html",
            	    	    "/notification-management.html",
            	    	    "/css/**",
            	    	    "/js/**",
            	    	    "/images/**",
            	    	    "/favicon.ico"
            	    	).permitAll()

            	    // Everything else requires authentication
            	    .anyRequest().authenticated()
            	)
            .exceptionHandling(exception -> exception
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write(
                        "{\"error\":\"FORBIDDEN\",\"message\":\"You do not have permission to access this resource.\"}"
                    );
                })
            );

        http.addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}