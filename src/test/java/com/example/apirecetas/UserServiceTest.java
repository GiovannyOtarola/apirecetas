package com.example.apirecetas;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;


import com.example.apirecetas.model.User;
import com.example.apirecetas.repository.UserRepository;

import com.example.apirecetas.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
     @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
    }

    @Test
    void testRegisterUser_Success() {
        // Simular que el usuario no existe en la base de datos
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        // Simular el cifrado de la contraseña
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encryptedPassword");

        // Simular el guardado del usuario
        when(userRepository.save(user)).thenReturn(user);

        // Llamar al método bajo prueba
        userService.registerUser(user);

        // Verificar que se realizaron las acciones esperadas
        verify(userRepository).existsByUsername(user.getUsername());
        verify(userRepository).existsByEmail(user.getEmail());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);

        // Comprobar que la contraseña del usuario fue cifrada
        assertEquals("encryptedPassword", user.getPassword());
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() {
        // Simular que el nombre de usuario ya existe
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        // Verificar que se lanza una excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user);
        });

        assertEquals("El nombre de usuario ya está en uso.", exception.getMessage());

        // Verificar que no se intentó guardar al usuario ni cifrar la contraseña
        verify(userRepository).existsByUsername(user.getUsername());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Simular que el correo ya existe
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // Verificar que se lanza una excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user);
        });

        assertEquals("El correo ya está en uso.", exception.getMessage());

        // Verificar que no se intentó guardar al usuario ni cifrar la contraseña
        verify(userRepository).existsByUsername(user.getUsername());
        verify(userRepository).existsByEmail(user.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
