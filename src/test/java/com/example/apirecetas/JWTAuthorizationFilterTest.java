package com.example.apirecetas;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.apirecetas.security.JWTAuthorizationFilter;

import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JWTAuthorizationFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Claims claims;

    @InjectMocks
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsJWTValid_withValidToken() {
        // Arrange
        String header = "Bearer valid_token";
        when(request.getHeader("Authorization")).thenReturn(header);

        // Act
        boolean isValid = jwtAuthorizationFilter.isJWTValid(request);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsJWTValid_withInvalidToken() {
        // Arrange
        String header = "invalid_token";
        when(request.getHeader("Authorization")).thenReturn(header);

        // Act
        boolean isValid = jwtAuthorizationFilter.isJWTValid(request);

        // Assert
        assertFalse(isValid);
    }



    @Test
    void testSetAuthentication() {
        // Arrange
        String username = "testUser";
        when(claims.getSubject()).thenReturn(username);
        when(claims.get("authorities")).thenReturn(List.of("ROLE_USER"));
        
        // Act
        jwtAuthorizationFilter.setAuthentication(claims);
        
        // Assert
        // Verificar que el contexto de seguridad se haya configurado correctamente
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void testDoFilterInternal_expiredJwtException() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer expired_token");
        doThrow(new ExpiredJwtException(null, null, "Token expired")).when(filterChain).doFilter(request, response);

        // Act & Assert
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);
        verify(response, times(1)).sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid compact JWT string: Compact JWSs must contain exactly 2 period characters, and compact JWEs must contain exactly 4.  Found: 0");
    }

    @Test
    void testDoFilterInternal_malformedJwtException() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer malformed_token");
        doThrow(new MalformedJwtException("Malformed JWT")).when(filterChain).doFilter(request, response);

        // Act & Assert
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);
        verify(response, times(1)).sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid compact JWT string: Compact JWSs must contain exactly 2 period characters, and compact JWEs must contain exactly 4.  Found: 0");
    }

    @Test
    void testDoFilterInternal_unsupportedJwtException() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer unsupported_token");
        doThrow(new UnsupportedJwtException("Unsupported JWT")).when(filterChain).doFilter(request, response);

        // Act & Assert
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);
        verify(response, times(1)).sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid compact JWT string: Compact JWSs must contain exactly 2 period characters, and compact JWEs must contain exactly 4.  Found: 0");
    }
}