package com.example.apirecetas.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


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
    private String urlVideo;
    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComentarioValoracion> comentariosValoraciones;

    public Receta(){}

    public Receta(String nombre, String paisOrigen, String tipoCocina, String dificultad) {
        this.nombre = nombre;
        this.paisOrigen = paisOrigen;
        this.tipoCocina = tipoCocina;
        this.dificultad = dificultad;
    }
}
