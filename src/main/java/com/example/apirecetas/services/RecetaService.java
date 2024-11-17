package com.example.apirecetas.services;
import com.example.apirecetas.model.ComentarioValoracion;
import com.example.apirecetas.model.ComentarioValoracionView;
import com.example.apirecetas.repository.CometarioValoracionRepository;

import org.springframework.stereotype.Service;
import java.util.Map;


import java.util.HashMap;
import java.util.List;


import com.example.apirecetas.repository.RecetaRepository;
import com.example.apirecetas.model.Receta;

@Service
public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final CometarioValoracionRepository cometarioValoracionRepository;
    private static final String RECETA_NO_ENCONTRADA = "Receta no encontrada";
   
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
        return recetaRepository.findById(id).orElseThrow(() -> new RuntimeException(RECETA_NO_ENCONTRADA));
    }

    // Método para obtener los detalles específicos de una receta
    public Map<String, Object> detalleReceta(Long id) {
        Receta receta = recetaRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException(RECETA_NO_ENCONTRADA));
        
        // Crear un mapa con los detalles necesarios
        Map<String, Object> detalles = new HashMap<>();
        detalles.put("id", receta.getId());
        detalles.put("dificultad", receta.getDificultad());
        detalles.put("ingredientes", receta.getIngredientes());
        detalles.put("instrucciones", receta.getInstrucciones());
        detalles.put("tiempoCoccion", receta.getTiempoCoccion());
        detalles.put("fotografiaUrl", receta.getFotografiaUrl());
        detalles.put("urlVideo", receta.getUrlVideo());

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
            throw new IllegalArgumentException("Comentarios y valoraciones no encontrados para la receta con id: " + id);
        }
        return comentarios;
    }




    public ComentarioValoracion guardarComentarioValoracion(ComentarioValoracion body) {
        if (body.getComentario() == null || body.getComentario().isEmpty()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío.");
        }
    
        if (body.getValoracion() == null || body.getValoracion() < 1 || body.getValoracion() > 5) {
            throw new IllegalArgumentException("La valoración debe estar entre 1 y 5.");
        }
    
        return cometarioValoracionRepository.save(body);
    }

    

    public void agregarVideo(Long recetaId, String videoUrl) {
        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new RuntimeException(RECETA_NO_ENCONTRADA));
        receta.setUrlVideo(videoUrl);
        recetaRepository.save(receta);
    }




}