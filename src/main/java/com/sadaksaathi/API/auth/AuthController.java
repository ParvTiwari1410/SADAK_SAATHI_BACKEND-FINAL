package com.sadaksaathi.API.auth;

import com.sadaksaathi.API.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // All endpoints in this class will start with /api/auth
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup") // This method handles POST requests to /api/auth/signup
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        try {
            User registeredUser = authService.registerUser(signUpRequest);
            // If successful, return the new user with a 200 OK status
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalStateException e) {
            // If the email is already in use, return the error message with a 400 Bad Request status
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * This is the new endpoint for handling user login.
     * @param loginRequest The email and password from the mobile app.
     * @return The authenticated User object or an error.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User loggedInUser = authService.loginUser(loginRequest);
            // If successful, return the logged-in user with a 200 OK status
            return ResponseEntity.ok(loggedInUser);
        } catch (IllegalStateException e) {
            // If credentials are wrong, return the error with a 401 Unauthorized status
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}