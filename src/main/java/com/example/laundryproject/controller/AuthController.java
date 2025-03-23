package com.example.laundryproject.controller;

import com.example.laundryproject.dto.LoginRequest;
import com.example.laundryproject.model.Auth;
import com.example.laundryproject.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from React app
public class AuthController {

    @Autowired
    private AuthService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Auth user) {
        try {
            if (userService.findByUsername(user.getUsername()) != null) {
                return ResponseEntity.badRequest().body("Username is already taken!");
            }
            if (userService.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.badRequest().body("Email is already registered!");
            }

            // Save the new user with hashed password
            Auth registeredUser = userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred during registration.");
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Auth user = userService.findByEmail(loginRequest.getEmail());

            if (user == null) {
                return ResponseEntity.badRequest().body("Email not found!");
            }

            // Verify password using BCrypt
            if (!userService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid password!");
            }

            // Return success response (Consider returning a JWT token)
            return ResponseEntity.ok("Login successful!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred during login.");
        }
    }
}
