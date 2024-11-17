package com.example.apirecetas.controller;

import com.example.apirecetas.model.ComentarioValoracion;
import com.example.apirecetas.model.ComentarioValoracionView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.apirecetas.model.Receta;
import com.example.apirecetas.services.RecetaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/private")
@CrossOrigin(origins = "http://localhost:8082")
public class PrivateController {
    
    private final RecetaService recetaService;

    
    public PrivateController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @GetMapping("/recetas")
    public List<Receta> listarRecetas() {
        return  recetaService.listarRecetas();
    }

    @GetMapping("/receta/{id}")
    public Receta getRecetaDetails(@PathVariable Long id) {

        return recetaService.getRecetaById(id);
    }

    @GetMapping("/recetas/{id}/detalle")
    public Map<String, Object> getDetalleReceta(@PathVariable Long id) {

        return recetaService.detalleReceta(id);
    }

    @PostMapping("/publicar")
    public ResponseEntity<String> crearReceta(@RequestBody Receta nuevaReceta) {
        if (nuevaReceta == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se recibió el cuerpo de la receta.");
        }
        try {
            recetaService.crearReceta(nuevaReceta);
            return ResponseEntity.status(HttpStatus.CREATED).body("Receta creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la receta: " + e.getMessage());
        }
    }


    @GetMapping("/receta/{id}/comentariosValoracion")
    public List<ComentarioValoracionView> listarCometariosValoracion(@PathVariable Long id) {
        return  recetaService.getComentarioValoracionByRecetaId(id);
    }


    @PostMapping("/recetas/{id}/guardarComentarioValoracion")
public ResponseEntity<ComentarioValoracionView> guardarComentarioValoracion(
        @PathVariable Long id, 
        @RequestBody ComentarioValoracion comentario) {

    // Buscar la receta por id
    Receta receta = recetaService.getRecetaById(id);

    if (receta == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Si no se encuentra la receta, devolver error 404
    }

    // Asociar la receta al comentario
    comentario.setReceta(receta);

    // Guardar el comentario y la valoración
    ComentarioValoracion savedComentario = recetaService.guardarComentarioValoracion(comentario);

    // Crear una vista simplificada con la interfaz ComentarioValoracionView
    ComentarioValoracionView comentarioView = new ComentarioValoracionView() {
        @Override
        public Long getId() {
            return savedComentario.getId();
        }

        @Override
        public String getComentario() {
            return savedComentario.getComentario();
        }

        @Override
        public Long getValoracion() {
            return savedComentario.getValoracion();
        }

        @Override
        public Long getRecetaId() {
            return savedComentario.getReceta().getId();
        }
    };

    // Retornar el comentario como una vista
    return ResponseEntity.ok(comentarioView);
}

    @PostMapping("/recetas/{id}/agregarVideo")
    public ResponseEntity<String> agregarVideo(@PathVariable Long id, @RequestParam String videoUrl) {
        try {
            recetaService.agregarVideo(id, videoUrl);
            return ResponseEntity.ok("URL del video agregada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al agregar el video: " + e.getMessage());
        }
    }
}
