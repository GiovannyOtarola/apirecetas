package com.example.apirecetas.services;
import org.springframework.stereotype.Service;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import com.example.apirecetas.repository.RecetaRepository;
import com.example.apirecetas.model.Receta;

@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;


    public List<Receta> listarRecetas(){
        return (List<Receta>) recetaRepository.findAll();
    }
    public List<Map<String, Object>> getRecetasRecientes() {
        return recetaRepository.findRecetasRecientes()
                .stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getRecetasPopulares() {
        return recetaRepository.findRecetasPopulares()
                .stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    private Map<String, Object> convertToMap(Object[] recetaData) {
        return Map.of(
                "id", recetaData[0],
                "nombre", recetaData[1],
                "paisOrigen", recetaData[2],
                "tipoCocina", recetaData[3],
                "dificultad", recetaData[4]
        );
    }

    public List<Receta> buscarRecetas(String nombre, String tipoCocina, String paisOrigen, String dificultad) {
        return recetaRepository.findByNombreAndTipoCocinaAndPaisOrigenAndDificultad(
            nombre, tipoCocina, paisOrigen, dificultad);
    }

    public Receta getRecetaById(Long id) {
        return recetaRepository.findById(id).orElseThrow(() -> new RuntimeException("Receta no encontrada"));
    }

    // Método para obtener los detalles específicos de una receta
    public Map<String, Object> detalleReceta(Long id) {
        Receta receta = recetaRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Receta no encontrada"));
        
        // Crear un mapa con los detalles necesarios
        Map<String, Object> detalles = new HashMap<>();
        detalles.put("dificultad", receta.getDificultad());
        detalles.put("ingredientes", receta.getIngredientes());
        detalles.put("instrucciones", receta.getInstrucciones());
        detalles.put("tiempoCoccion", receta.getTiempoCoccion());
        detalles.put("fotografiaUrl", receta.getFotografiaUrl());

        return detalles;
    }
}