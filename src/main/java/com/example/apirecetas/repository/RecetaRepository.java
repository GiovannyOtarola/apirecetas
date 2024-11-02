package com.example.apirecetas.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.example.apirecetas.model.*;
import org.springframework.data.repository.query.Param;

public interface RecetaRepository extends CrudRepository<Receta, Long> {

    @Query("SELECT r.id, r.nombre, r.paisOrigen, r.tipoCocina, r.dificultad FROM Receta r ORDER BY r.id DESC")
    List<Object[]> findRecetasRecientes();

    @Query("SELECT r.id, r.nombre, r.paisOrigen, r.tipoCocina, r.dificultad FROM Receta r ORDER BY r.id DESC")
    List<Object[]> findRecetasPopulares();

    @Query("SELECT new Receta(r.nombre, r.tipoCocina, r.paisOrigen, r.dificultad) " +
            "FROM Receta r " +
            "WHERE (:nombre IS NULL OR r.nombre = :nombre) " +
            "AND (:tipoCocina IS NULL OR r.tipoCocina = :tipoCocina) " +
            "AND (:paisOrigen IS NULL OR r.paisOrigen = :paisOrigen) " +
            "AND (:dificultad IS NULL OR r.dificultad = :dificultad)")
    List<Receta> findByNombreAndTipoCocinaAndPaisOrigenAndDificultad(
            @Param("nombre") String nombre,
            @Param("tipoCocina") String tipoCocina,
            @Param("paisOrigen") String paisOrigen,
            @Param("dificultad") String dificultad
    );

//    @Query("SELECT r.id, r.nombre, r.paisOrigen, r.tipoCocina, r.dificultad FROM Receta r " +
//            "WHERE (:nombre IS NULL OR r.nombre LIKE %:nombre%) " +
//            "AND (:tipoCocina IS NULL OR r.tipoCocina = :tipoCocina) " +
//            "AND (:paisOrigen IS NULL OR r.paisOrigen = :paisOrigen) " +
//            "AND (:dificultad IS NULL OR r.dificultad = :dificultad)")
//    List<Object[]> findRecetasByFields(
//            @Param("nombre") String nombre,
//            @Param("tipoCocina") String tipoCocina,
//            @Param("paisOrigen") String paisOrigen,
//            @Param("dificultad") String dificultad);

    @Query("SELECT r.id, r.nombre, r.paisOrigen, r.tipoCocina, r.dificultad FROM Receta r " +
            "WHERE (:nombre IS NULL OR :nombre = '' OR r.nombre LIKE %:nombre%) " +
            "AND (:tipoCocina IS NULL OR :tipoCocina = '' OR r.tipoCocina = :tipoCocina) " +
            "AND (:paisOrigen IS NULL OR :paisOrigen = '' OR r.paisOrigen = :paisOrigen) " +
            "AND (:dificultad IS NULL OR :dificultad = '' OR r.dificultad = :dificultad)")
    List<Object[]> findRecetasByFields(
            @Param("nombre") String nombre,
            @Param("tipoCocina") String tipoCocina,
            @Param("paisOrigen") String paisOrigen,
            @Param("dificultad") String dificultad);



}
