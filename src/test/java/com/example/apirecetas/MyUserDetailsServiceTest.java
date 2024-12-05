package com.example.apirecetas;


import com.example.apirecetas.model.User;
import com.example.apirecetas.repository.UserRepository;
import com.example.apirecetas.services.impl.MyUserDetailsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;  // Mock the UserRepository

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;  // Inject mocks into the service

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks

        // Prepare a sample user for the tests
        user = new User();
        user.setId((int) 1L);
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setName("Test User");
        user.setEmail("test@example.com");
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        // Arrange
        String username = "testUser";
        when(userRepository.findByUsername(username)).thenReturn(user);  // Mock the repository

        // Act
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);  // Ensure userDetails is not null
        assertEquals(username, userDetails.getUsername());  // Check if username matches
        assertEquals("password123", userDetails.getPassword());  // Check if password matches
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(null);  // Mock the repository to return null

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername(username);  // Should throw exception
        });
    }
}