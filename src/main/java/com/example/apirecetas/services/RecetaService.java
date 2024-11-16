package com.example.apirecetas.services;
import com.example.apirecetas.model.ComentarioValoracion;
import com.example.apirecetas.model.ComentarioValoracionView;
import com.example.apirecetas.repository.CometarioValoracionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Map;


import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.apirecetas.repository.RecetaRepository;
import com.example.apirecetas.model.Receta;

@Service
public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final CometarioValoracionRepository cometarioValoracionRepository;

   
    public RecetaService(RecetaRepository recetaRepository,CometarioValoracionRepository cometarioValoracionRepository) {
        this.recetaRepository = recetaRepository;
        this.cometarioValoracionRepository = cometarioValoracionRepository;
    }

    public List<Receta> listarRecetas(){

        return (List<Receta>) recetaRepository.findAll();
    }
    public List<Map<String, Object>> getRecetasRecientes() {
        return recetaRepository.findRecetasRecientes()
                .stream()
                .map(this::convertToMap)
                .toList();
    }

    public List<Map<String, Object>> getRecetasPopulares() {
        return recetaRepository.findRecetasPopulares()
                .stream()
                .map(this::convertToMap)
                .toList();
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

    public List<Map<String, Object>> buscarRecetas(String nombre, String tipoCocina, String paisOrigen, String dificultad) {
        return recetaRepository.findRecetasByFields(
                        nombre, tipoCocina, paisOrigen, dificultad)
                        .stream()
                        .map(this::convertToMap)
                        .toList();
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
        detalles.put("id", receta.getId());
        detalles.put("dificultad", receta.getDificultad());
        detalles.put("ingredientes", receta.getIngredientes());
        detalles.put("instrucciones", receta.getInstrucciones());
        detalles.put("tiempoCoccion", receta.getTiempoCoccion());
        detalles.put("fotografiaUrl", receta.getFotografiaUrl());

        return detalles;
    }

    public void crearReceta(Receta receta){
        recetaRepository.save(receta);
    }

    public List<ComentarioValoracion> listarComentarioValoracion(){

        return (List<ComentarioValoracion>) cometarioValoracionRepository.findAll();
    }


    public List<ComentarioValoracionView> getComentarioValoracionByRecetaId(Long id) {
        List<ComentarioValoracionView> comentarios = cometarioValoracionRepository.findComentarioValoracionByRecetaId(id);
        if (comentarios.isEmpty()) {
            throw new RuntimeException("Comentarios y valoraciones no encontrados para la receta con id: " + id);
        }
        return comentarios;
    }




    public ComentarioValoracion guardarComentarioValoracion(ComentarioValoracion body) {

        return cometarioValoracionRepository.save(body);
    }






}