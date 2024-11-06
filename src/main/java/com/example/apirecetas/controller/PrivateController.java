package com.example.apirecetas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apirecetas.model.Receta;
import com.example.apirecetas.services.RecetaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/private")
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
}
