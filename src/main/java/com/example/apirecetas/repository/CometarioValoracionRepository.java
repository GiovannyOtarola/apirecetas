package com.example.apirecetas.repository;

import com.example.apirecetas.model.ComentarioValoracion;
import com.example.apirecetas.model.ComentarioValoracionView;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CometarioValoracionRepository extends CrudRepository<ComentarioValoracion, Long> {

    @Query("SELECT c.id AS id, c.comentario AS comentario, c.valoracion AS valoracion, c.receta.id AS recetaId, c.aprobado AS aprobado " +
       "FROM ComentarioValoracion c WHERE c.receta.id = :id AND c.aprobado = true")
    List<ComentarioValoracionView> findComentarioValoracionByRecetaId(@Param("id") Long recetaId);




}
