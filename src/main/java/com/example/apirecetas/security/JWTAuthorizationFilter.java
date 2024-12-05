package com.example.apirecetas.security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import javax.crypto.SecretKey;
import static com.example.apirecetas.contants.Constants.*;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    public Claims setSigningKey(HttpServletRequest request) {
        String jwtToken = request.
                getHeader(HEADER_AUTHORIZACION_KEY).
                replace(TOKEN_BEARER_PREFIX, "");

        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey(KEY))
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

    }


    public void setAuthentication(Claims claims) {

        @SuppressWarnings("unchecked")
        List<String> authorities =(List<String>) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                    authorities.stream().map(SimpleGrantedAuthority::new).toList());

        SecurityContextHolder.getContext().setAuthentication(auth);

    }


    public boolean isJWTValid(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(HEADER_AUTHORIZACION_KEY);
        return authenticationHeader != null && authenticationHeader.startsWith(TOKEN_BEARER_PREFIX);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request,  HttpServletResponse response,  FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isJWTValid(request)) {
                Claims claims = setSigningKey(request);
                if (claims.get("authorities") != null) {
                    setAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

}