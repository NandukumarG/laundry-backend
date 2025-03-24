package com.example.laundryproject.service;

import com.example.laundryproject.dto.UserRequest;
import com.example.laundryproject.model.User;
import com.example.laundryproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Transactional
    public User updateUserProfile(String email, String name, String newEmail, String phone, String address, String avatarUrl) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        if (name != null && !name.isBlank()) {
            user.setFullName(name);
        }

        if (newEmail != null && !newEmail.isBlank() && !newEmail.equals(email)) {
            // Check if new email is already taken
            if (userRepository.existsByEmail(newEmail)) {
                throw new RuntimeException("Email is already in use");
            }
            user.setEmail(newEmail);
        }

        if (phone != null) {
            user.setPhone(phone);
        }

        if (address != null) {
            user.setAddress(address);
        }

        if (avatarUrl != null && !avatarUrl.isBlank()) {
            user.setAvatarUrl(avatarUrl);
        }

        return userRepository.save(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public UserRequest getUserProfile(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }

        return new UserRequest(user.getFullName(), user.getEmail(), user.getPhone(), user.getAddress(), user.getAvatarUrl());
    }
}