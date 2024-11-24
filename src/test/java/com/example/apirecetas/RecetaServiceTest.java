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
        // Crear un arreglo de objetos para simular los datos de la receta
        Object[] recetaData = {1L, "Paella", "España", "Mediterránea", "Media"};
        
        // Crear una lista de Object[] para que coincida con el tipo esperado
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(recetaData);

        // Simular la llamada al repositorio
        when(recetaRepository.findRecetasRecientes()).thenReturn(mockResult);

        // Llamada al servicio
        List<Map<String, Object>> recientes = recetaService.getRecetasRecientes();

        // Asegurarse de que la lista tenga un solo elemento y de que los valores sean correctos
        assertEquals(1, recientes.size());
        assertEquals("Paella", recientes.get(0).get("nombre"));
        assertEquals("España", recientes.get(0).get("paisOrigen"));
        assertEquals("Mediterránea", recientes.get(0).get("tipoCocina"));
        assertEquals("Media", recientes.get(0).get("dificultad"));

        // Verificar que la función del repositorio fue llamada correctamente
        verify(recetaRepository).findRecetasRecientes();
    }

    @Test
    void testGetRecetasPopulares() {
        // Crear un arreglo de objetos para simular los datos de la receta
        Object[] recetaData = {1L, "Paella", "España", "Mediterránea", "Media"};

        // Crear una lista de Object[] para que coincida con el tipo esperado
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(recetaData);

        // Simular la llamada al repositorio
        when(recetaRepository.findRecetasPopulares()).thenReturn(mockResult);


        // Llamada al servicio
        List<Map<String, Object>> populares = recetaService.getRecetasPopulares();

        // Asegurarse de que la lista tenga un solo elemento y de que los valores sean correctos
        assertEquals(1, populares.size());
        assertEquals("Paella", populares.get(0).get("nombre"));
        assertEquals("España", populares.get(0).get("paisOrigen"));
        assertEquals("Mediterránea", populares.get(0).get("tipoCocina"));
        assertEquals("Media", populares.get(0).get("dificultad"));

        // Verificar que la función del repositorio fue llamada correctamente
        verify(recetaRepository).findRecetasPopulares();
    }

    @Test
    void testBuscarRecetas() {
        // Crear un arreglo de objetos para simular los datos de la receta
        Object[] recetaData = {1L, "Paella", "España", "Mediterránea", "Media"};
    
        // Crear una lista de Object[] para que coincida con el tipo esperado
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(recetaData);
    
        // Simular la llamada al repositorio
        when(recetaRepository.findRecetasByFields("Paella", "Mediterránea", "España", "Media"))
                .thenReturn(mockResult);
    
        // Llamada al servicio
        List<Map<String, Object>> resultados = recetaService.buscarRecetas("Paella", "Mediterránea", "España", "Media");
    
        // Asegurarse de que la lista tenga un solo elemento y de que los valores sean correctos
        assertEquals(1, resultados.size());
        assertEquals("Paella", resultados.get(0).get("nombre"));
        assertEquals("España", resultados.get(0).get("paisOrigen"));
        assertEquals("Mediterránea", resultados.get(0).get("tipoCocina"));
        assertEquals("Media", resultados.get(0).get("dificultad"));
    
        // Verificar que la función del repositorio fue llamada correctamente
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
    void testGuardarComentarioValoracion_Success() {
        ComentarioValoracion comentario = new ComentarioValoracion();
        comentario.setComentario("Muy buena receta");
        comentario.setValoracion((long) 5);

        when(cometarioValoracionRepository.save(comentario)).thenReturn(comentario);

        ComentarioValoracion guardado = recetaService.guardarComentarioValoracion(comentario);

        assertEquals("Muy buena receta", guardado.getComentario());
        verify(cometarioValoracionRepository).save(comentario);
    }

    @Test
    void testGuardarComentarioValoracion_InvalidComentario() {
        ComentarioValoracion comentario = new ComentarioValoracion();
        comentario.setComentario("");
        comentario.setValoracion((long) 5);

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
        comentario.setValoracion((long) 6);

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
