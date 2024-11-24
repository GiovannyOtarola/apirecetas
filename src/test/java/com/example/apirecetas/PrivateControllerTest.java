package com.example.apirecetas;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
import com.example.apirecetas.model.ComentarioValoracionView;
import com.example.apirecetas.model.Receta;

import com.example.apirecetas.services.RecetaService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PrivateControllerTest {
    @Mock
    private RecetaService recetaService;

    @InjectMocks
    private PrivateController privateController;

    // Datos de ejemplo
    private Receta receta;
    private ComentarioValoracion comentario;
    private ComentarioValoracionView comentarioView;

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
    void testGuardarComentarioValoracion_Success() {
        when(recetaService.getRecetaById(1L)).thenReturn(receta);
        when(recetaService.guardarComentarioValoracion(comentario)).thenReturn(comentario);

        ResponseEntity<ComentarioValoracionView> response = privateController.guardarComentarioValoracion(1L, comentario);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Muy buena receta", response.getBody().getComentario());
        verify(recetaService).getRecetaById(1L);
        verify(recetaService).guardarComentarioValoracion(comentario);
    }

    @Test
    void testGuardarComentarioValoracion_InvalidRequest() {
        ComentarioValoracion invalidComentario = new ComentarioValoracion();
        ResponseEntity<ComentarioValoracionView> response = privateController.guardarComentarioValoracion(1L, invalidComentario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(recetaService);
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

}
