package com.fitconnect.service;

import com.fitconnect.dto.*;
import com.fitconnect.model.User;
import com.fitconnect.repository.UserRepository;
import com.fitconnect.security.JwtUtils;
import com.fitconnect.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthService - Handles user registration and login
 */
@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtils jwtUtils;

    /**
     * Register a new user
     */
    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        // Create new user with encoded password
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : User.Role.USER);
        user.setFitnessGoal(request.getFitnessGoal());
        user.setLocation(request.getLocation());

        User saved = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtils.generateTokenFromEmail(saved.getEmail());

        return new AuthResponse(token, saved.getId(), saved.getName(),
                               saved.getEmail(), saved.getRole().name());
    }

    /**
     * Authenticate user and return JWT
     */
    public AuthResponse login(AuthRequest request) {
        // Authenticate with Spring Security
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Generate JWT token
        String token = jwtUtils.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new AuthResponse(token, userDetails.getId(), userDetails.getName(),
                               userDetails.getEmail(), userDetails.getRole());
    }
}
