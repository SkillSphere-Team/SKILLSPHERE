package com.skillsphere.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillsphere.exception.ResourceNotFoundException;
import com.skillsphere.user.dto.UpdateUserRequest;
import com.skillsphere.user.entity.Role;
import com.skillsphere.user.entity.RoleName;
import com.skillsphere.user.entity.User;
import com.skillsphere.user.repository.RoleRepository;
import com.skillsphere.user.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public UserService(
	        UserRepository userRepository,
	        RoleRepository roleRepository) {

	    this.userRepository = userRepository;
	    this.roleRepository = roleRepository;
	}

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id
                ));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + email
                ));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    public User updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        ));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        return userRepository.save(user);
    }
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        ));

        userRepository.delete(user);
    }
    
    public User updateUserStatus(Long id, boolean active) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        ));

        user.setActive(active);

        return userRepository.save(user);
    }
    public User updateUserRole(Long id, RoleName roleName) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        ));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found: " + roleName
                        ));

        user.getRoles().clear();

        user.getRoles().add(role);

        return userRepository.save(user);
    }
}