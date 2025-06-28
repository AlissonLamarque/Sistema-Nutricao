package com.example.sistemanutricao.repository;

import com.example.sistemanutricao.model.IngredientesPorFicha;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface IngredientesPorFichaRepository extends JpaRepository<IngredientesPorFicha, Long> {

    List<IngredientesPorFicha> findByFichaTecnicaId(Long fichaId);

    @Modifying
    @Transactional
    void deleteByFichaTecnica_IdAndIdNotIn(Long fichaTecnicaId, List<Long> ids);

}