package com.example.apirecetas;

import org.junit.jupiter.api.Test;

import com.example.apirecetas.model.ComentarioValoracionDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ComentarioValoracionDTOTest {
    
    @Test
    void testComentarioValoracionDTOConstructorAndGetters() {
        // Arrange
        Long expectedId = 1L;
        String expectedComentario = "Excelente receta!";
        Long expectedValoracion = 5L;
        boolean expectedAprobado = true;

        // Act
        ComentarioValoracionDTO comentarioValoracionDTO = new ComentarioValoracionDTO(
                expectedId, expectedComentario, expectedValoracion, expectedAprobado
        );

        // Assert
        assertEquals(expectedId, comentarioValoracionDTO.getId());
        assertEquals(expectedComentario, comentarioValoracionDTO.getComentario());
        assertEquals(expectedValoracion, comentarioValoracionDTO.getValoracion());
        assertTrue(comentarioValoracionDTO.isAprobado());
    }

    @Test
    void testSetters() {
        // Arrange
        ComentarioValoracionDTO comentarioValoracionDTO = new ComentarioValoracionDTO();

        // Act
        comentarioValoracionDTO.setId(2L);
        comentarioValoracionDTO.setComentario("Muy buena receta!");
        comentarioValoracionDTO.setValoracion(4L);
        comentarioValoracionDTO.setAprobado(false);

        // Assert
        assertEquals(2L, comentarioValoracionDTO.getId());
        assertEquals("Muy buena receta!", comentarioValoracionDTO.getComentario());
        assertEquals(4L, comentarioValoracionDTO.getValoracion());
        assertFalse(comentarioValoracionDTO.isAprobado());
    }
}
