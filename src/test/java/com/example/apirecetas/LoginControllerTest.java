package com.example.apirecetas;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.apirecetas.controller.LoginController;
import com.example.apirecetas.model.AuthResponse;
import com.example.apirecetas.model.User;
import com.example.apirecetas.security.JWTAuthtenticationConfig;

import com.example.apirecetas.services.impl.MyUserDetailsService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {
    
    @Mock
    private JWTAuthtenticationConfig jwtAuthtenticationConfig;

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginController loginController;

    @Test
    void loginWithValidCredentialsAndEncryptedPassword() {
        // Arrange
        String username = "testUser";
        String password = "encryptedPass";
        String encryptedPassword = "$2a$10$abcdef...";
        String jwtToken = "mockedJwtToken";
        String role = "USER";

        // Crear una instancia real de User
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setRole(role);

        // Configurar los mocks
        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(password, encryptedPassword)).thenReturn(true);
        when(jwtAuthtenticationConfig.getJWTToken(user)).thenReturn(jwtToken);

        // Act
        ResponseEntity<AuthResponse> response = loginController.login(username, password);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(jwtToken, response.getBody().getToken());
        assertEquals("ROLE_USER", response.getBody().getRole());
    }

    @Test
    void loginWithInvalidPassword() {
        // Arrange
        String username = "testUser";
        String password = "wrongPass";
        String encryptedPassword = "$2a$10$abcdef...";

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.getPassword()).thenReturn(encryptedPassword);
        when(passwordEncoder.matches(password, encryptedPassword)).thenReturn(false);

        // Act
        ResponseEntity<AuthResponse> response = loginController.login(username, password);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void loginWithUserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        String password = "anyPassword";
    
        // Configurar el mock para lanzar UsernameNotFoundException
        when(userDetailsService.loadUserByUsername(username))
                .thenThrow(new UsernameNotFoundException("User not found"));
    
        // Act
        ResponseEntity<AuthResponse> response = null;
        try {
            response = loginController.login(username, password);
        } catch (UsernameNotFoundException e) {
            // Manejar la excepción durante el test
            assertEquals("User not found", e.getMessage());
        }
    
        // Assert
        assertNull(response, "La respuesta debería ser nula debido a un usuario inexistente");
    }
    
}
