package com.example.apirecetas;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.apirecetas.controller.PublicController;
import com.example.apirecetas.model.User;
import com.example.apirecetas.services.RecetaService;
import com.example.apirecetas.services.UserService;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class PublicControllerTest {
     @Mock
    private RecetaService recetaService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PublicController publicController;

    @Test
    void testGetHomePage() {
        // Datos simulados
        List<Map<String, Object>> recetasRecientes = List.of(Map.of("id", 1, "nombre", "Receta 1"));
        List<Map<String, Object>> recetasPopulares = List.of(Map.of("id", 2, "nombre", "Receta 2"));
        
        // Mocking del servicio
        when(recetaService.getRecetasRecientes()).thenReturn(recetasRecientes);
        when(recetaService.getRecetasPopulares()).thenReturn(recetasPopulares);

        // Llamar al método
        Map<String, Object> response = publicController.getHomePage();

        // Verificar la respuesta
        assertThat(response).isNotNull();
        assertThat(response.get("recetasRecientes")).isEqualTo(recetasRecientes);
        assertThat(response.get("recetasPopulares")).isEqualTo(recetasPopulares);
        assertThat(response.get("banners")).isEqualTo(List.of("Banner 1", "Banner 2"));
    }

    @Test
    void testBuscarRecetas() {
        // Datos simulados
        List<Map<String, Object>> recetas = List.of(
            Map.of("id", 1, "nombre", "Receta 1", "paisOrigen", "México"),
            Map.of("id", 2, "nombre", "Receta 2", "paisOrigen", "España")
        );

        // Mocking del servicio
        when(recetaService.buscarRecetas("Receta", "Mediterránea", "España", "Media")).thenReturn(recetas);

        // Llamar al método
        List<Map<String, Object>> resultado = publicController.buscarRecetas("Receta", "Mediterránea", "España", "Media");

        // Verificar la respuesta
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).get("nombre")).isEqualTo("Receta 1");
    }

    @Test
    void testRegisterUser_Success() {
        // Datos simulados
        User user = new User("testuser1","testuser1", "test@gmail.com", "password123");

        // Mocking del servicio (sin excepción)
        doNothing().when(userService).registerUser(user);

        // Llamar al método
        ResponseEntity<String> response = publicController.registerUser(user);

        // Verificar la respuesta
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Usuario registrado con éxito.");
    }

    @Test
    void testRegisterUser_Failure() {
        // Datos simulados
        User user = new User("testuser2","testuser2", "test@gmail.com", "password123");

        // Mocking del servicio (simular excepción)
        doThrow(new RuntimeException("El usuario ya existe")).when(userService).registerUser(user);

        // Llamar al método
        ResponseEntity<String> response = publicController.registerUser(user);

        // Verificar la respuesta
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Error en el registro: El usuario ya existe");
    }
}
