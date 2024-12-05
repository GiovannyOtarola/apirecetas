package com.example.apirecetas;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import com.example.apirecetas.model.ComentarioValoracion;
import com.example.apirecetas.model.Receta;

import com.example.apirecetas.repository.CometarioValoracionRepository;
import com.example.apirecetas.repository.RecetaRepository;
import com.example.apirecetas.services.RecetaService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class RecetaServiceTest {

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private CometarioValoracionRepository cometarioValoracionRepository;

    @InjectMocks
    private RecetaService recetaService;

    private Receta receta;

    @BeforeEach
    void setUp() {
        receta = new Receta();
        receta.setId(1L);
        receta.setNombre("Paella");
        receta.setPaisOrigen("España");
        receta.setTipoCocina("Mediterránea");
        receta.setDificultad("Media");
        receta.setIngredientes("Arroz, mariscos, especias");
        receta.setInstrucciones("Cocinar a fuego lento.");
        receta.setTiempoCoccion(45);
        receta.setFotografiaUrl("http://example.com/paella.jpg");
    }

    @Test
    void testListarRecetas() {
        when(recetaRepository.findAll()).thenReturn(List.of(receta));

        List<Receta> recetas = recetaService.listarRecetas();

        assertEquals(1, recetas.size());
        assertEquals("Paella", recetas.get(0).getNombre());
        verify(recetaRepository).findAll();
    }

    @Test
    void testGetRecetasRecientes() {
        Object[] recetaData = {1L, "Paella", "España", "Mediterránea", "Media"};
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(recetaData);

        when(recetaRepository.findRecetasRecientes()).thenReturn(mockResult);

        List<Map<String, Object>> recientes = recetaService.getRecetasRecientes();

        assertEquals(1, recientes.size());
        assertEquals("Paella", recientes.get(0).get("nombre"));
        assertEquals("España", recientes.get(0).get("paisOrigen"));
        assertEquals("Mediterránea", recientes.get(0).get("tipoCocina"));
        assertEquals("Media", recientes.get(0).get("dificultad"));

        verify(recetaRepository).findRecetasRecientes();
    }

    @Test
    void testGetRecetasPopulares() {
        Object[] recetaData = {1L, "Paella", "España", "Mediterránea", "Media"};
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(recetaData);

        when(recetaRepository.findRecetasPopulares()).thenReturn(mockResult);

        List<Map<String, Object>> populares = recetaService.getRecetasPopulares();

        assertEquals(1, populares.size());
        assertEquals("Paella", populares.get(0).get("nombre"));
        assertEquals("España", populares.get(0).get("paisOrigen"));
        assertEquals("Mediterránea", populares.get(0).get("tipoCocina"));
        assertEquals("Media", populares.get(0).get("dificultad"));

        verify(recetaRepository).findRecetasPopulares();
    }

    @Test
    void testBuscarRecetas() {
        Object[] recetaData = {1L, "Paella", "España", "Mediterránea", "Media"};
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(recetaData);

        when(recetaRepository.findRecetasByFields("Paella", "Mediterránea", "España", "Media"))
                .thenReturn(mockResult);

        List<Map<String, Object>> resultados = recetaService.buscarRecetas("Paella", "Mediterránea", "España", "Media");

        assertEquals(1, resultados.size());
        assertEquals("Paella", resultados.get(0).get("nombre"));
        assertEquals("España", resultados.get(0).get("paisOrigen"));
        assertEquals("Mediterránea", resultados.get(0).get("tipoCocina"));
        assertEquals("Media", resultados.get(0).get("dificultad"));

        verify(recetaRepository).findRecetasByFields("Paella", "Mediterránea", "España", "Media");
    }

    @Test
    void testGetRecetaById_Success() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        Receta encontrada = recetaService.getRecetaById(1L);

        assertEquals("Paella", encontrada.getNombre());
        verify(recetaRepository).findById(1L);
    }

    @Test
    void testGetRecetaById_NotFound() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            recetaService.getRecetaById(1L);
        });

        assertEquals("Receta no encontrada", exception.getMessage());
        verify(recetaRepository).findById(1L);
    }

    @Test
    void testDetalleReceta_Success() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        Map<String, Object> detalles = recetaService.detalleReceta(1L);

        assertEquals(1L, detalles.get("id"));
        assertEquals("Media", detalles.get("dificultad"));
        assertEquals("Arroz, mariscos, especias", detalles.get("ingredientes"));
        assertEquals("Cocinar a fuego lento.", detalles.get("instrucciones"));
        assertEquals(45, detalles.get("tiempoCoccion"));
        assertEquals("http://example.com/paella.jpg", detalles.get("fotografiaUrl"));
        assertEquals(null, detalles.get("urlVideo"));  // Si no se ha asignado video aún

        verify(recetaRepository).findById(1L);
    }

    @Test
    void testDetalleReceta_NotFound() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            recetaService.detalleReceta(1L);
        });

        assertEquals("Receta no encontrada", exception.getMessage());
        verify(recetaRepository).findById(1L);
    }

    @Test
    void testGuardarComentarioValoracion_Success() {
        ComentarioValoracion comentario = new ComentarioValoracion();
        comentario.setComentario("Muy buena receta");
        comentario.setValoracion(5L);

        when(cometarioValoracionRepository.save(comentario)).thenReturn(comentario);

        ComentarioValoracion guardado = recetaService.guardarComentarioValoracion(comentario);

        assertEquals("Muy buena receta", guardado.getComentario());
        verify(cometarioValoracionRepository).save(comentario);
    }

    @Test
    void testGuardarComentarioValoracion_InvalidComentario() {
        ComentarioValoracion comentario = new ComentarioValoracion();
        comentario.setComentario("");
        comentario.setValoracion(5L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            recetaService.guardarComentarioValoracion(comentario);
        });

        assertEquals("El comentario no puede estar vacío.", exception.getMessage());
        verify(cometarioValoracionRepository, never()).save(any());
    }

    @Test
    void testGuardarComentarioValoracion_InvalidValoracion() {
        ComentarioValoracion comentario = new ComentarioValoracion();
        comentario.setComentario("Regular receta");
        comentario.setValoracion(6L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            recetaService.guardarComentarioValoracion(comentario);
        });

        assertEquals("La valoración debe estar entre 1 y 5.", exception.getMessage());
        verify(cometarioValoracionRepository, never()).save(any());
    }

    @Test
    void testAgregarVideo_Success() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        recetaService.agregarVideo(1L, "http://example.com/video.mp4");

        assertEquals("http://example.com/video.mp4", receta.getUrlVideo());
        verify(recetaRepository).findById(1L);
        verify(recetaRepository).save(receta);
    }

    @Test
    void testAgregarVideo_RecetaNotFound() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            recetaService.agregarVideo(1L, "http://example.com/video.mp4");
        });

        assertEquals("Receta no encontrada", exception.getMessage());
        verify(recetaRepository).findById(1L);
        verify(recetaRepository, never()).save(any());
    }

    

}