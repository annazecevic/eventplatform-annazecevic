package com.example.demo.controller;

import com.example.demo.config.JwtService;
import com.example.demo.dto.LoginDTO;
import com.example.demo.model.Administrator;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }

        UserDetails user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Determine role
        boolean isAdmin = user instanceof Administrator;

        // Optional: add claims to JWT
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("isAdmin", isAdmin);

        String token = jwtService.generateToken(extraClaims, user);

        // Return token + role + name
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", isAdmin ? "ADMIN" : "USER");
        response.put("name", user.getUsername());

        return response;
    }



}
