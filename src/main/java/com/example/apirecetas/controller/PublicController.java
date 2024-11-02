package com.example.apirecetas.controller;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.apirecetas.services.RecetaService;
import com.example.apirecetas.model.Receta;
import java.util.Map;

@RestController
@RequestMapping("/public")

public class PublicController {

    @Autowired
    private RecetaService recetaService;

    @GetMapping("/home")
    public Map<String, Object> getHomePage() {
        List<Map<String, Object>> recetasRecientes = recetaService.getRecetasRecientes();
        List<Map<String, Object>> recetasPopulares = recetaService.getRecetasPopulares();
        List<String> banners = List.of("Banner 1", "Banner 2");

        Map<String, Object> response = new HashMap<>();
        response.put("recetasRecientes", recetasRecientes);
        response.put("recetasPopulares", recetasPopulares);
        response.put("banners", banners);

        return response;
    }

    @GetMapping("/buscar")
    public List<Map<String, Object>> buscarRecetas(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "tipoCocina", required = false) String tipoCocina,
            @RequestParam(value = "paisOrigen", required = false) String paisOrigen,
            @RequestParam(value = "dificultad", required = false) String dificultad) {

        return recetaService.buscarRecetas(nombre, tipoCocina, paisOrigen, dificultad);
    }
}