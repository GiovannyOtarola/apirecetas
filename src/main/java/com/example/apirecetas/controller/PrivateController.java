package com.example.apirecetas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apirecetas.model.Receta;
import com.example.apirecetas.services.RecetaService;

@RestController
@RequestMapping("/private")
public class PrivateController {
    
     @Autowired
    private RecetaService recetaService;

    @GetMapping("/recetas/{id}")
    public Receta getRecetaDetails(@PathVariable Long id) {
        return recetaService.getRecetaById(id);
    }
}
