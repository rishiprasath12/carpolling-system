package com.app.carpolling.service;

import com.app.carpolling.dto.AuthResponse;
import com.app.carpolling.dto.LoginRequest;
import com.app.carpolling.dto.UserRegistrationRequest;
import com.app.carpolling.entity.User;
import com.app.carpolling.repository.UserRepository;
import com.app.carpolling.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    
    @Transactional
    public AuthResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone already registered");
        }
        
        // Check if email is provided and already exists
        if (request.getEmail() != null && !request.getEmail().isEmpty() 
            && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        
        // Validate password strength
        validatePassword(request.getPassword());

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail()); // Can be null
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setIsActive(true);
        
        User savedUser = userRepository.save(user);

        return new AuthResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(),
            savedUser.getPhone(), null, savedUser.getRole());
    }
    
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByPhone(request.getPhone())
            .orElseThrow(() -> new RuntimeException("User not found with this phone number"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        if (!user.getIsActive()) {
            throw new RuntimeException("User account is inactive");
        }

        String token = jwtUtils.generateToken(user.getPhone());

        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone(),
            token, user.getRole());
    }
    
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters long");
        }
        
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> 
            "!@#$%^&*()_+-=[]{}|;':\"\\,.<>/?".indexOf(ch) >= 0
        );
        
        if (!hasUpperCase) {
            throw new RuntimeException("Password must contain at least one uppercase letter");
        }
        if (!hasDigit) {
            throw new RuntimeException("Password must contain at least one number");
        }
        if (!hasSpecialChar) {
            throw new RuntimeException("Password must contain at least one special character (!@#$%^&*()_+-=[]{}|;':\"\\,.<>/?)");
        }
    }
}




