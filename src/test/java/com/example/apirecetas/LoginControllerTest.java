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

import com.example.apirecetas.security.JWTAuthtenticationConfig;

import com.example.apirecetas.services.impl.MyUserDetailsService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void testLogin_Success() {
        String username = "testuser";
        String password = "plaintextpassword";
        String encryptedPassword = "$2a$10$encryptedpassword";
        String token = "mocked.jwt.token";

        UserDetails userDetails = mock(UserDetails.class);
        
        when(userDetails.getPassword()).thenReturn(encryptedPassword);

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, encryptedPassword)).thenReturn(true);
        
        
        when(jwtAuthtenticationConfig.getJWTToken(username)).thenReturn(token);

        ResponseEntity<String> response = loginController.login(username, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody());
    }

    @Test
    void testLogin_Failure_InvalidPassword() {
        // Datos simulados
        String username = "testuser";
        String password = "wrongpassword";
        String encryptedPassword = "$2a$10$encryptedpassword";

        UserDetails userDetails = mock(UserDetails.class);
        
        when(userDetails.getPassword()).thenReturn(encryptedPassword);

        // Mocking servicios
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, encryptedPassword)).thenReturn(false);

        // Llamar al método
        ResponseEntity<String> response = loginController.login(username, password);

        // Verificar resultados
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Usuario y/o clave incorrecto", response.getBody());
    }

    @Test
    void testLogin_Failure_UserNotFound() {
        // Datos simulados
        String username = "unknownuser";
        String password = "anyPassword";

        // Mocking servicios
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new UsernameNotFoundException("Usuario no encontrado"));

        // Llamar al método
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            loginController.login(username, password);
        });

        // Verificar resultados
        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void testLogin_Failure_UnencryptedPassword() {
        // Datos simulados
        String username = "testuser";
        String password = "wrongpassword";
        String plainPassword = "plaintextpassword";

        UserDetails userDetails = mock(UserDetails.class);
        
        when(userDetails.getPassword()).thenReturn(plainPassword);

        // Mocking servicios
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Llamar al método
        ResponseEntity<String> response = loginController.login(username, password);

        // Verificar resultados
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Usuario y/o clave incorrecto", response.getBody());
    }
    
}
