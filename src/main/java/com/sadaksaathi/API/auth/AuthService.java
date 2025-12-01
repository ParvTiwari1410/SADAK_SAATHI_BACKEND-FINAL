package com.sadaksaathi.API.auth;

import com.sadaksaathi.API.user.User;
import com.sadaksaathi.API.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Marks this class as a Spring service component
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring automatically provides the UserRepository and PasswordEncoder
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(SignUpRequest signUpRequest) {
        // Check if a user with the given email already exists
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }

        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPhone(signUpRequest.getPhone());
        user.setCity(signUpRequest.getCity());
        // Securely hash the password before saving it
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        // Save the new user to the database
        return userRepository.save(user);
    }

    /**
     * This is the new method for handling user login.
     * @param loginRequest The email and password from the mobile app.
     * @return The authenticated User object.
     */
    public User loginUser(LoginRequest loginRequest) {
        // 1. Find the user by email. If not found, throw an error.
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalStateException("User not found with email: " + loginRequest.getEmail()));

        // 2. Check if the provided password matches the stored hashed password.
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalStateException("Incorrect password");
        }

        // 3. If the password is correct, return the User object.
        return user;
    }
}