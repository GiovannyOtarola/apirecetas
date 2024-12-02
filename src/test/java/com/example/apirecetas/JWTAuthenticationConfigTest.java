package com.example.apirecetas;


import com.example.apirecetas.model.User;
import com.example.apirecetas.security.JWTAuthtenticationConfig;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class JWTAuthenticationConfigTest {

    @SuppressWarnings("unchecked")
    @Test
    void testGetJWTToken() {
        // Mockear Jwts.builder()
        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            // Mockear el builder de JWT
            JwtBuilder mockJwtBuilder = mock(JwtBuilder.class);
            when(mockJwtBuilder.claims(any(Map.class))).thenReturn(mockJwtBuilder);
            when(mockJwtBuilder.subject(anyString())).thenReturn(mockJwtBuilder);
            when(mockJwtBuilder.issuedAt(any(Date.class))).thenReturn(mockJwtBuilder);
            when(mockJwtBuilder.expiration(any(Date.class))).thenReturn(mockJwtBuilder);
            when(mockJwtBuilder.signWith(any())).thenReturn(mockJwtBuilder);
            when(mockJwtBuilder.compact()).thenReturn("mocked-jwt-token");

            // Configurar el mock estático
            mockedJwts.when(Jwts::builder).thenReturn(mockJwtBuilder);

            // Crear instancia de JWTAuthenticationConfig
            JWTAuthtenticationConfig jwtConfig = new JWTAuthtenticationConfig();

            // Crear un usuario simulado
            User mockUser = new User();
            mockUser.setUsername("testuser");
            mockUser.setRole("USER");

            // Ejecutar el método
            String token = jwtConfig.getJWTToken(mockUser);

            // Verificar el resultado
            assertNotNull(token);
            assertNotNull(token.startsWith("Bearer "));
        }
    }
}