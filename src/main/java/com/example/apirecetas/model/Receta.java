package com.example.apirecetas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class Receta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipoCocina;
    private String paisOrigen;
    private String dificultad;
    private String ingredientes;
    private String instrucciones;
    private int tiempoCoccion;
    private String fotografiaUrl;
}
