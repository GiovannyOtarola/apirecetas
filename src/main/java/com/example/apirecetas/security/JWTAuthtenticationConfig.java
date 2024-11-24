package com.example.apirecetas.security;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.example.apirecetas.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.example.apirecetas.contants.Constants.KEY;
import static com.example.apirecetas.contants.Constants.getSigningKey;


@Configuration
public class JWTAuthtenticationConfig {

        public String getJWTToken(User user) {
                // Obtener el rol del usuario (por ejemplo, "ROLE_ADMIN" o "ROLE_USER")
                String role = user.getRole(); 
        
                // Crear la lista de autoridades basadas en el rol
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                        .commaSeparatedStringToAuthorityList("ROLE_" + role.toUpperCase());

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        String token = Jwts.builder()
                .claims(claims) 
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1440))
                .signWith(getSigningKey(KEY))
                .compact();

        return "Bearer " + token;
    }


}
