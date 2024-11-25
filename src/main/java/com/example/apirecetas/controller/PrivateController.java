package com.example.apirecetas.controller;

import com.example.apirecetas.model.ComentarioValoracion;
import com.example.apirecetas.model.ComentarioValoracionView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.apirecetas.model.Receta;
import com.example.apirecetas.model.User;
import com.example.apirecetas.services.RecetaService;
import com.example.apirecetas.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/private")
@CrossOrigin(origins = "http://localhost:8082")
public class PrivateController {
    
    private final RecetaService recetaService;
    private final UserService userService;
    
    public PrivateController(RecetaService recetaService, UserService userService) {
        this.recetaService = recetaService;
        this.userService = userService;
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
        try {
            return  recetaService.getComentarioValoracionByRecetaId(id);
        }catch (Exception ex){
            return new ArrayList<>();
        }
    }


    @PostMapping("/recetas/{id}/guardarComentarioValoracion")
    public ResponseEntity<ComentarioValoracionView> guardarComentarioValoracion(
            @PathVariable Long id, 
            @RequestBody ComentarioValoracion comentario) {

        // Validar el cuerpo de la solicitud
        if (comentario == null || comentario.getComentario() == null || comentario.getComentario().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (comentario.getValoracion() == null || comentario.getValoracion() < 1 || comentario.getValoracion() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Buscar la receta por id
        Receta receta = recetaService.getRecetaById(id);
        if (receta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Si no se encuentra la receta, devolver error 404
        }

        // Asociar la receta al comentario
        comentario.setReceta(receta);

        try {
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
            return ResponseEntity.status(HttpStatus.CREATED).body(comentarioView);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // O puedes devolver un mensaje de error
        }
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

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(List.of()); 
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser (@PathVariable Integer id, @RequestBody User user) {
        // Lógica para actualizar el usuario. Asegúrate de que el ID coincida.
        User existingUser  = userService.findById(id);
        if (existingUser  == null) {
            return ResponseEntity.notFound().build();
        }

        existingUser .setUsername(user.getUsername());
        existingUser .setEmail(user.getEmail());
        existingUser .setName(user.getName());
        existingUser .setRole(user.getRole());
        userService.updateUser (existingUser ); // Implementa este método en UserService

        return ResponseEntity.ok(existingUser );
    }
}
