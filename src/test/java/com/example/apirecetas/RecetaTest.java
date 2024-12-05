package com.example.apirecetas;


import org.junit.jupiter.api.Test;

import com.example.apirecetas.model.Receta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RecetaTest {

    @Test
    void testRecetaConstructor() {
        // Arrange
        String nombre = "Paella";
        String tipoCocina = "Mediterránea";
        String paisOrigen = "España";
        String dificultad = "Alta";

        // Act
        Receta receta = new Receta(nombre, paisOrigen, tipoCocina, dificultad);

        // Assert
        assertNotNull(receta);
        assertEquals(nombre, receta.getNombre());
        assertEquals(tipoCocina, receta.getTipoCocina());
        assertEquals(paisOrigen, receta.getPaisOrigen());
        assertEquals(dificultad, receta.getDificultad());
    }

    @Test
    void testRecetaSettersAndGetters() {
        // Arrange
        Receta receta = new Receta();

        // Act
        receta.setNombre("Tacos");
        receta.setTipoCocina("Mexicana");
        receta.setPaisOrigen("México");
        receta.setDificultad("Media");
        receta.setIngredientes("Tortillas, Carne, Cebolla");
        receta.setInstrucciones("Cocinar la carne, montar los tacos");
        receta.setTiempoCoccion(30);
        receta.setFotografiaUrl("url_fotografia");
        receta.setUrlVideo("url_video");

        // Assert
        assertEquals("Tacos", receta.getNombre());
        assertEquals("Mexicana", receta.getTipoCocina());
        assertEquals("México", receta.getPaisOrigen());
        assertEquals("Media", receta.getDificultad());
        assertEquals("Tortillas, Carne, Cebolla", receta.getIngredientes());
        assertEquals("Cocinar la carne, montar los tacos", receta.getInstrucciones());
        assertEquals(30, receta.getTiempoCoccion());
        assertEquals("url_fotografia", receta.getFotografiaUrl());
        assertEquals("url_video", receta.getUrlVideo());
    }
}