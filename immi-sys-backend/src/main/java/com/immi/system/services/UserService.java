package com.immi.system.services;

import com.immi.system.enums.RoleEnum;
import com.immi.system.models.UserModel;
import com.immi.system.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    // Method to register a new user with the given data
    public void registerNewUser(String firstName, String lastName, String email, String password, RoleEnum role) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered.");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        UserModel newUser = new UserModel(null, firstName, lastName, email, encodedPassword, role, true);
        userRepository.save(newUser);
    }

    // Fetch a user by email for authentication purposes
    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Fetch a user by ID for other operations (e.g., updating user details)
    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Check if user is active
    public boolean isUserActive(Long userId) {
        return userRepository.findById(userId)
                .map(UserModel::isActive)
                .orElse(false);
    }

}
