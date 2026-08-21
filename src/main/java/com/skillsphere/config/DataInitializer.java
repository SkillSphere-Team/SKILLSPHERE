package com.skillsphere.config;

import com.skillsphere.user.entity.Role;
import com.skillsphere.user.entity.RoleName;
import com.skillsphere.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeRoles(RoleRepository roleRepository) {

        return args -> {

            for (RoleName roleName : RoleName.values()) {

                if (roleRepository.findByName(roleName).isEmpty()) {

                    Role role = new Role();
                    role.setName(roleName);

                    roleRepository.save(role);
                }
            }
        };
    }
}