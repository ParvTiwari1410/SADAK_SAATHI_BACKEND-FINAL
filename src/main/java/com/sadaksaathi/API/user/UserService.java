package com.sadaksaathi.API.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ✅ Signup
    public User registerUser(User user) {
        // Prevent duplicate email registration
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Email already registered");
        }
        return userRepository.save(user);
    }

    // ✅ Login
    public Optional<User> login(String email, String password) {
        if (email == null || password == null) {
            return Optional.empty();
        }

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            User foundUser = user.get();

            if (foundUser.getPassword() != null && foundUser.getPassword().equals(password)) {
                return Optional.of(foundUser);
            }
        }

        return Optional.empty();
    }
}
