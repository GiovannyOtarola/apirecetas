package com.example.apirecetas.controller;


import com.example.apirecetas.security.JWTAuthtenticationConfig;
import com.example.apirecetas.services.impl.MyUserDetailsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final JWTAuthtenticationConfig jwtAuthtenticationConfig;
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    
    
    public LoginController(JWTAuthtenticationConfig jwtAuthtenticationConfig, MyUserDetailsService userDetailsService,PasswordEncoder passwordEncoder) {
        this.jwtAuthtenticationConfig = jwtAuthtenticationConfig;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam("user") String username,
            @RequestParam("encryptedPass") String password) {



        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario y/o clave incorrecto");
            }

        String token = jwtAuthtenticationConfig.getJWTToken(username);

        return ResponseEntity.ok(token);

    }

}
