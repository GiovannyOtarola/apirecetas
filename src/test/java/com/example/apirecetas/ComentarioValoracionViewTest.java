package com.example.apirecetas;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.apirecetas.model.ComentarioValoracion;
import com.example.apirecetas.model.ComentarioValoracionView;
import com.example.apirecetas.model.Receta;

public class ComentarioValoracionViewTest {

    private ComentarioValoracionView comentarioView;
    private Receta receta;
    private ComentarioValoracion comentario;

    @BeforeEach
    void setUp() {
        // Inicializamos los objetos para la prueba
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
    void testGetId() {
        assertEquals(1L, comentarioView.getId());
    }

    @Test
    void testGetComentario() {
        assertEquals("Muy buena receta", comentarioView.getComentario());
    }

    @Test
    void testGetValoracion() {
        assertEquals(5L, comentarioView.getValoracion());
    }

    @Test
    void testGetRecetaId() {
        assertEquals(1L, comentarioView.getRecetaId());
    }
}