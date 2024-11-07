package com.example.apirecetas.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apirecetas.model.User;
import com.example.apirecetas.services.RecetaService;
import com.example.apirecetas.services.UserService;

import java.util.Map;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "http://localhost:8082")
public class PublicController {

     private final RecetaService recetaService;
    private final UserService userService;

    public PublicController(RecetaService recetaService, UserService userService) {
        this.recetaService = recetaService;
        this.userService = userService;
    }

   
    
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

    @PostMapping("/registro")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Usuario registrado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el registro: " + e.getMessage());
        }
    }
}