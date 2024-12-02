package com.example.apirecetas;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.apirecetas.model.UserDTO;

public class UserDTOTest {
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Inicializamos un objeto UserDTO con valores de prueba
        userDTO = new UserDTO(1, "user1", "User One", "user1@example.com", "USER");
    }

    @Test
    void testGetId() {
        assertEquals(1, userDTO.getId());
    }

    @Test
    void testGetUsername() {
        assertEquals("user1", userDTO.getUsername());
    }

    @Test
    void testGetName() {
        assertEquals("User One", userDTO.getName());
    }

    @Test
    void testGetEmail() {
        assertEquals("user1@example.com", userDTO.getEmail());
    }

    @Test
    void testGetRole() {
        assertEquals("USER", userDTO.getRole());
    }

    @Test
    void testSetId() {
        userDTO.setId(2);
        assertEquals(2, userDTO.getId());
    }

    @Test
    void testSetUsername() {
        userDTO.setUsername("user2");
        assertEquals("user2", userDTO.getUsername());
    }

    @Test
    void testSetName() {
        userDTO.setName("User Two");
        assertEquals("User Two", userDTO.getName());
    }

    @Test
    void testSetEmail() {
        userDTO.setEmail("user2@example.com");
        assertEquals("user2@example.com", userDTO.getEmail());
    }

    @Test
    void testSetRole() {
        userDTO.setRole("ADMIN");
        assertEquals("ADMIN", userDTO.getRole());
    }

    @Test
    void testConstructor() {
        // Verificamos que el constructor esté asignando correctamente los valores
        UserDTO userDTO = new UserDTO(2, "user2", "User Two", "user2@example.com", "ADMIN");
        
        assertEquals(2, userDTO.getId());
        assertEquals("user2", userDTO.getUsername());
        assertEquals("User Two", userDTO.getName());
        assertEquals("user2@example.com", userDTO.getEmail());
        assertEquals("ADMIN", userDTO.getRole());
    }
}
