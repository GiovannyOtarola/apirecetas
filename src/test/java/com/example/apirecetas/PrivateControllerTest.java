package com.example.apirecetas;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.apirecetas.controller.PrivateController;

import com.example.apirecetas.model.ComentarioValoracion;
import com.example.apirecetas.model.ComentarioValoracionDTO;
import com.example.apirecetas.model.ComentarioValoracionView;
import com.example.apirecetas.model.Receta;
import com.example.apirecetas.model.User;
import com.example.apirecetas.model.UserDTO;
import com.example.apirecetas.services.RecetaService;
import com.example.apirecetas.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PrivateControllerTest {
   @Mock
    private RecetaService recetaService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PrivateController privateController;

    private Receta receta;
    private ComentarioValoracion comentario;
    private ComentarioValoracionView comentarioView;
    private User user;
    private ComentarioValoracion comentarioActualizado;

    @BeforeEach
    void setUp() {
        receta = new Receta();
        receta.setId(1L);
        receta.setNombre("Tacos");

        comentario = new ComentarioValoracion();
        comentario.setId(1L);
        comentario.setComentario("Muy buena receta");
        comentario.setValoracion(5L);
        comentario.setReceta(receta);

        comentarioView = new ComentarioValoracionView() {
            @Override
            public Long getId() {
                return comentario.getId();
            }

            @Override
            public String getComentario() {
                return comentario.getComentario();
            }

            @Override
            public Long getValoracion() {
                return comentario.getValoracion();
            }

            @Override
            public Long getRecetaId() {
                return receta.getId();
            }
        };
        comentarioActualizado = new ComentarioValoracion();
        comentarioActualizado.setId(1L);
        comentarioActualizado.setComentario("Comentario actualizado");
        comentarioActualizado.setValoracion(4L);
        comentarioActualizado.setAprobado(false);

        user = new User();
        user.setId(1);
        user.setUsername("user1");
        user.setName("User One");
        user.setEmail("user1@example.com");
        user.setRole("USER");
    }

    

    @Test
    void testListarRecetas() {
        List<Receta> recetas = List.of(receta);
        when(recetaService.listarRecetas()).thenReturn(recetas);

        List<Receta> result = privateController.listarRecetas();

        assertEquals(1, result.size());
        assertEquals("Tacos", result.get(0).getNombre());
        verify(recetaService).listarRecetas();
    }

    @Test
    void testGetRecetaDetails() {
        when(recetaService.getRecetaById(1L)).thenReturn(receta);

        Receta result = privateController.getRecetaDetails(1L);

        assertNotNull(result);
        assertEquals("Tacos", result.getNombre());
        verify(recetaService).getRecetaById(1L);
    }

    @Test
    void testGetDetalleReceta() {
        Map<String, Object> detalle = Map.of("nombre", "Tacos", "dificultad", "Fácil");
        when(recetaService.detalleReceta(1L)).thenReturn(detalle);

        Map<String, Object> result = privateController.getDetalleReceta(1L);

        assertEquals("Tacos", result.get("nombre"));
        assertEquals("Fácil", result.get("dificultad"));
        verify(recetaService).detalleReceta(1L);
    }

    @Test
    void testCrearReceta_Success() {
        doNothing().when(recetaService).crearReceta(any(Receta.class));

        ResponseEntity<String> response = privateController.crearReceta(receta);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Receta creada exitosamente", response.getBody());
        verify(recetaService).crearReceta(receta);
    }

    @Test
    void testCrearReceta_NullRequest() {
        ResponseEntity<String> response = privateController.crearReceta(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No se recibió el cuerpo de la receta.", response.getBody());
    }

    @Test
    void testListarCometariosValoracion() {
        List<ComentarioValoracionView> comentarios = List.of(comentarioView);
        when(recetaService.getComentarioValoracionByRecetaId(1L)).thenReturn(comentarios);

        List<ComentarioValoracionView> result = privateController.listarCometariosValoracion(1L);

        assertEquals(1, result.size());
        assertEquals("Muy buena receta", result.get(0).getComentario());
        verify(recetaService).getComentarioValoracionByRecetaId(1L);
    }

    @Test
    void testGetAllComentarios() {
        // Creamos una lista de ComentarioValoracion como mock
        ComentarioValoracion comentario1 = new ComentarioValoracion();
        comentario1.setId(1L);
        comentario1.setComentario("Comentario 1");
        comentario1.setValoracion(5L);
        comentario1.setAprobado(true);

        ComentarioValoracion comentario2 = new ComentarioValoracion();
        comentario2.setId(2L);
        comentario2.setComentario("Comentario 2");
        comentario2.setValoracion(4L);
        comentario2.setAprobado(true);

        List<ComentarioValoracion> comentarios = List.of(comentario1, comentario2);
        
        // Simulamos que el servicio devuelve estos comentarios
        when(recetaService.getAllComentarios()).thenReturn(comentarios);

        // Llamamos al método del controlador
        ResponseEntity<List<ComentarioValoracionDTO>> response = privateController.getAllComentarios();

        // Afirmamos que la respuesta tenga código 200 (OK)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verificamos que el cuerpo de la respuesta contiene los DTOs de los comentarios
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Comentario 1", response.getBody().get(0).getComentario());
        assertEquals(5L, response.getBody().get(0).getValoracion());

        // Verificamos que se haya llamado al servicio para obtener los comentarios
        verify(recetaService).getAllComentarios();
    }

    @Test
    void testUpdateComentario_Success() {
        // Simula que el comentario existe en la base de datos
        when(recetaService.findById(1L)).thenReturn(comentario);
        when(recetaService.updateComentarios(any(ComentarioValoracion.class))).thenReturn(comentarioActualizado);

        // Llamamos al método del controlador
        ResponseEntity<ComentarioValoracion> response = privateController.updateComentario(1L, comentarioActualizado);

        // Verificamos que la respuesta sea 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comentario actualizado", response.getBody().getComentario());
        assertEquals(4L, response.getBody().getValoracion());
        assertFalse(response.getBody().isAprobado());

        // Verificamos que se haya llamado al servicio para actualizar el comentario
        verify(recetaService).updateComentarios(any(ComentarioValoracion.class));
    }

    @Test
    void testUpdateComentario_NotFound() {
        // Simula que el comentario no existe en la base de datos
        when(recetaService.findById(1L)).thenReturn(null);

        // Llamamos al método del controlador
        ResponseEntity<ComentarioValoracion> response = privateController.updateComentario(1L, comentarioActualizado);

        // Verificamos que la respuesta sea 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verificamos que no se haya llamado al servicio para actualizar el comentario
        verify(recetaService, times(0)).updateComentarios(any(ComentarioValoracion.class));
    }

    @Test
    void testAgregarVideo_Success() {
        doNothing().when(recetaService).agregarVideo(1L, "http://video.url");

        ResponseEntity<String> response = privateController.agregarVideo(1L, "http://video.url");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("URL del video agregada exitosamente", response.getBody());
        verify(recetaService).agregarVideo(1L, "http://video.url");
    }

    @Test
    void testAgregarVideo_Error() {
        doThrow(new RuntimeException("Error al agregar el video"))
                .when(recetaService).agregarVideo(1L, "http://video.url");

        ResponseEntity<String> response = privateController.agregarVideo(1L, "http://video.url");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al agregar el video: Error al agregar el video", response.getBody());
        verify(recetaService).agregarVideo(1L, "http://video.url");
    }

    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        ResponseEntity<List<UserDTO>> response = privateController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("user1", response.getBody().get(0).getUsername());
    }

    @Test
    void testUpdateUser() {
        when(userService.findById(1)).thenReturn(user);
        when(userService.updateUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = privateController.updateUser(1, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user1", response.getBody().getUsername());
        verify(userService).updateUser(any(User.class));
    }

   @Test
    void testUpdateUser_NotFound() {
        // Simula que el método findById devuelve null, indicando que el usuario no se encuentra
        when(userService.findById(1)).thenReturn(null);

        // Llamamos al método updateUser del controlador
        ResponseEntity<User> response = privateController.updateUser(1, user);

        // Afirmamos que el código de estado de la respuesta es 404 (No Encontrado)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verificamos que no se haya hecho ninguna otra interacción con userService después de la llamada
        verify(userService).findById(1); // Asegúrate de que findById fue llamado
        verifyNoMoreInteractions(userService); // Verifica que no haya más interacciones con el mock
    }

    @Test
    void testGuardarComentarioValoracion_Success() {
        when(recetaService.getRecetaById(1L)).thenReturn(receta);
        when(recetaService.guardarComentarioValoracion(comentario)).thenReturn(comentario);
    
        ResponseEntity<ComentarioValoracionView> response = privateController.guardarComentarioValoracion(1L, comentario);
    
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Muy buena receta", response.getBody().getComentario());
        verify(recetaService).guardarComentarioValoracion(comentario);
    }

    @Test
    void testGuardarComentarioValoracion_ValidRequest() {
        // Configurar comportamiento simulado del servicio
        when(recetaService.getRecetaById(1L)).thenReturn(receta);
        when(recetaService.guardarComentarioValoracion(any(ComentarioValoracion.class))).thenReturn(comentario);

        // Realizar la prueba
        ResponseEntity<ComentarioValoracionView> response = privateController.guardarComentarioValoracion(1L, comentario);

        // Validar resultados
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ComentarioValoracionView comentarioView = response.getBody();
        assertEquals(1L, comentarioView.getId());
        assertEquals("Muy buena receta", comentarioView.getComentario());
        assertEquals(5L, comentarioView.getValoracion());
        assertEquals(1L, comentarioView.getRecetaId());

        // Verificar interacciones
        verify(recetaService).getRecetaById(1L);
        verify(recetaService).guardarComentarioValoracion(any(ComentarioValoracion.class));
    }

    @Test
    void testGuardarComentarioValoracion_InvalidComentario_Null() {
        // Realizar la prueba con un comentario nulo
        ResponseEntity<ComentarioValoracionView> response = privateController.guardarComentarioValoracion(1L, null);

        // Validar resultados
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testGuardarComentarioValoracion_InvalidComentario_Empty() {
        // Configurar un comentario inválido
        comentario.setComentario("");

        // Realizar la prueba
        ResponseEntity<ComentarioValoracionView> response = privateController.guardarComentarioValoracion(1L, comentario);

        // Validar resultados
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

     @Test
    void testGuardarComentarioValoracion_InvalidValoracion() {
        // Configurar un comentario con valoración inválida
        comentario.setValoracion(6L);

        // Realizar la prueba
        ResponseEntity<ComentarioValoracionView> response = privateController.guardarComentarioValoracion(1L, comentario);

        // Validar resultados
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testGuardarComentarioValoracion_RecetaNotFound() {
        // Simular que la receta no existe
        when(recetaService.getRecetaById(1L)).thenReturn(null);

        // Realizar la prueba
        ResponseEntity<ComentarioValoracionView> response = privateController.guardarComentarioValoracion(1L, comentario);

        // Validar resultados
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());

        // Verificar interacciones
        verify(recetaService).getRecetaById(1L);
        verify(recetaService, never()).guardarComentarioValoracion(any(ComentarioValoracion.class));
    }

    @Test
    void testGuardarComentarioValoracion_InternalServerError() {
        // Configurar comportamiento simulado para lanzar excepción
        when(recetaService.getRecetaById(1L)).thenReturn(receta);
        when(recetaService.guardarComentarioValoracion(any(ComentarioValoracion.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Realizar la prueba
        ResponseEntity<ComentarioValoracionView> response = privateController.guardarComentarioValoracion(1L, comentario);

        // Validar resultados
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());

        // Verificar interacciones
        verify(recetaService).getRecetaById(1L);
        verify(recetaService).guardarComentarioValoracion(any(ComentarioValoracion.class));
    }

}
