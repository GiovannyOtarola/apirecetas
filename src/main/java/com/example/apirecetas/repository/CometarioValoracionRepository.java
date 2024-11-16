package com.example.apirecetas.repository;

import com.example.apirecetas.model.ComentarioValoracion;
import com.example.apirecetas.model.ComentarioValoracionView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CometarioValoracionRepository extends CrudRepository<ComentarioValoracion, Long> {

    @Query("SELECT c.id AS id, c.comentario AS comentario, c.valoracion AS valoracion, c.receta.id AS recetaId " +
            "FROM ComentarioValoracion c WHERE c.receta.id = :id")
    List<ComentarioValoracionView> findComentarioValoracionByRecetaId(@Param("id") Long recetaId);




}
