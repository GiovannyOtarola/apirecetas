package com.example.apirecetas.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.example.apirecetas.model.*;

public interface RecetaRepository extends CrudRepository<Receta, Long> {

    @Query("SELECT r.id, r.nombre, r.paisOrigen, r.tipoCocina, r.dificultad FROM Receta r ORDER BY r.id DESC")
    List<Object[]> findRecetasRecientes();

    @Query("SELECT r.id, r.nombre, r.paisOrigen, r.tipoCocina, r.dificultad FROM Receta r ORDER BY r.id DESC") 
    List<Object[]> findRecetasPopulares();

    List<Receta> findByNombreAndTipoCocinaAndPaisOrigenAndDificultad(
        String nombre, String tipoCocina, String paisOrigen, String dificultad);

}
