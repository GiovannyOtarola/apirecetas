package com.example.apirecetas.services;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import com.example.apirecetas.repository.RecetaRepository;
import com.example.apirecetas.model.Receta;

@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    public List<Receta> getRecetasRecientes() {
        return recetaRepository.findAll(); 
    }

    public List<Receta> getRecetasPopulares() {
        return recetaRepository.findAll(); 
    }

    public List<Receta> buscarRecetas(String nombre, String tipoCocina, String paisOrigen, String dificultad) {
        return recetaRepository.findByNombreAndTipoCocinaAndPaisOrigenAndDificultad(
            nombre, tipoCocina, paisOrigen, dificultad);
    }

    public Receta getRecetaById(Long id) {
        return recetaRepository.findById(id).orElseThrow(() -> new RuntimeException("Receta no encontrada"));
    }
}