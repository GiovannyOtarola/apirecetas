package com.example.apirecetas.controller;

import com.example.apirecetas.model.ApiResponse;
import com.example.apirecetas.security.JWTAuthtenticationConfig;
import com.example.apirecetas.services.impl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    JWTAuthtenticationConfig jwtAuthtenticationConfig;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam("user") String username,
            @RequestParam("encryptedPass") String encryptedPass) {

        /**
         * En el ejemplo no se realiza la correcta validación del usuario
         */

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!userDetails.getPassword().equals(encryptedPass)) {
            return ResponseEntity.ok("Token invalido");
        }

        String token = jwtAuthtenticationConfig.getJWTToken(username);

        return ResponseEntity.ok(token);

    }

}
