package com.example.apirecetas.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.example.apirecetas.model.*;

public interface RecetaRepository extends JpaRepository<Receta, Long> {
    List<Receta> findByNombreAndTipoCocinaAndPaisOrigenAndDificultad(
        String nombre, String tipoCocina, String paisOrigen, String dificultad);
}
