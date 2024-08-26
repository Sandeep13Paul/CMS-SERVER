package com.cms.Controllers;

import com.cms.Helpers.JWTUtilHelper;
import com.cms.Models.User;
import com.cms.Services.Impl.SecurityCustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private JWTUtilHelper jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityCustomUserDetailsService userDetailsService;

    // Endpoint to handle JWT-based login

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

            // Check password
            if (userDetailsService.checkPassword(user.getPassword(), userDetails.getPassword())) {
                // Generate JWT token
                String token = jwtUtil.generateToken(userDetails);

                // Return the token in response
                return ResponseEntity.ok(token);
            } else {
                // Invalid credentials
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e) {
            // Handle exception
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

}
