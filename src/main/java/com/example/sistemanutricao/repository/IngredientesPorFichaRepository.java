package com.example.sistemanutricao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.sistemanutricao.model.IngredientesPorFicha;

import jakarta.transaction.Transactional;

public interface IngredientesPorFichaRepository extends JpaRepository<IngredientesPorFicha, Long> {

    List<IngredientesPorFicha> findByFichaTecnicaId(Long fichaId);

    @Modifying
    @Transactional
    void deleteByFichaTecnica_IdAndIdNotIn(Long fichaTecnicaId, List<Long> ids);

    @Query("SELECT i FROM IngredientesPorFicha i WHERE i.fichaTecnica.id = :fichaId")
    List<IngredientesPorFicha> buscarPorFichaTecnicaId(@Param("fichaId") Long fichaId);

}