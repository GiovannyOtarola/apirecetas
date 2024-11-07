package com.example.apirecetas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
