package com.example.apirecetas.security;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.apirecetas.contants.Constants.SUPER_SECRET_KEY;
import static com.example.apirecetas.contants.Constants.getSigningKey;


@Configuration
public class JWTAuthtenticationConfig {

    public String getJWTToken(String username) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        String token = Jwts.builder()
                .setClaims(claims) // Usar setClaims en lugar de claims() y add()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1440))
                .signWith(getSigningKey(SUPER_SECRET_KEY))
                .compact();

        return "Bearer " + token;
    }


}
