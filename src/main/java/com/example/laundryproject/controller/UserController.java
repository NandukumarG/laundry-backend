package com.example.laundryproject.controller;

import com.example.laundryproject.dto.UserRequest;
import com.example.laundryproject.dto.UserResponse;
import com.example.laundryproject.model.User;
import com.example.laundryproject.service.UserService;
import com.example.laundryproject.util.FileUploadUtil;
import com.example.laundryproject.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from React app
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final FileUploadUtil fileUploadUtil;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, FileUploadUtil fileUploadUtil, JwtUtil jwtUtil) {
        this.userService = userService;
        this.fileUploadUtil = fileUploadUtil;
        this.jwtUtil = jwtUtil;
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            if (user.getPhone() == null || user.getPhone().isEmpty()) {
                // Generate a placeholder like "pending-USER123"
                user.setPhone("pending-" + System.currentTimeMillis());
            }

            if (userService.findByUsername(user.getUsername()) != null) {
                return ResponseEntity.badRequest().body("Username is already taken!");
            }
            if (userService.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.badRequest().body("Email is already registered!");
            }

            // Save the new user with hashed password
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Registration error: " + e.getMessage());
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserRequest loginRequest) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail());

            if (user == null) {
                return ResponseEntity.badRequest().body("Email not found!");
            }

            if (!userService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid password!");
            }

            String token = jwtUtil.generateToken(user.getEmail());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred during login.");
        }
    }

//    @GetMapping("/profile")
//    public ResponseEntity<?> getUserProfile(Authentication authentication) {
//        // Get authentication directly as a method parameter instead of from SecurityContextHolder
//        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
//            System.out.println("Authentication failed: " + (authentication == null ? "null" : authentication.getName()));
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }
//
//        String email = authentication.getName();
//        System.out.println("Getting profile for authenticated user: " + email);
//
//        User user = userService.findByEmail(email);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for email: " + email);
//        }
//        UserResponse profileResponse = new UserResponse(
//                user.getFullName(),
//                user.getEmail(),
//                user.getPhone(),
//                user.getAddress(),
//                user.getAvatarUrl()
//        );
//        return ResponseEntity.ok(profileResponse);
//    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Principal principal) {
        try {
            // Log principal details
            logger.info("Principal: {}", principal);

            if (principal == null) {
                logger.warn("No principal found");
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No authentication details"));
            }

            // Get username directly from Principal
            String username = principal.getName();
            logger.info("Username from principal: {}", username);

            // Fetch user profile
            UserRequest userProfile = userService.getUserProfile(username);

            if (userProfile == null) {
                logger.warn("No user profile found for username: {}", username);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "User profile not found"));
            }

            return ResponseEntity.ok(userProfile);
        } catch (Exception e) {
            logger.error("Error fetching user profile", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access denied", "details", e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserRequest profileRequest) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        System.out.println("Updating profile for user: " + currentUsername);

        String email = profileRequest.getEmail();

        try {
            User updatedUser = userService.updateUserProfile(
                    email,  // Use the email from request
                    profileRequest.getFullName(),
                    email,  // Use same email for new email (or handle differently)
                    profileRequest.getPhone(),
                    profileRequest.getAddress(),
                    profileRequest.getAvatarUrl()
            );

            UserResponse profileResponse = new UserResponse(
                    updatedUser.getFullName(),
                    updatedUser.getEmail(),
                    updatedUser.getPhone(),
                    updatedUser.getAddress(),
                    updatedUser.getAvatarUrl()
            );

            return ResponseEntity.ok(profileResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Failed to update profile: " + e.getMessage()));
        }
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userService.findByEmail(currentUsername);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        try {
            String fileName = user.getId() + "_" + System.currentTimeMillis() + "_profile.jpg";
            String avatarUrl = fileUploadUtil.saveFile(fileName, file);

            // Update user's avatar URL
            user.setAvatarUrl(avatarUrl);
            userService.saveUser(user);

            Map<String, String> response = new HashMap<>();
            response.put("avatarUrl", avatarUrl);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to upload avatar: " + e.getMessage()));
        }
    }
}
