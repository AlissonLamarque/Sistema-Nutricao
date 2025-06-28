package com.example.sistemanutricao.record.IngredientesPorFichaDTO;

import com.example.sistemanutricao.record.IngredienteDTO.IngredienteGetDTO;

import java.math.BigDecimal;

public record IngredientePorFichaGetDTO(
        Long id,
        IngredienteGetDTO ingrediente,
        BigDecimal custoKg,
        BigDecimal custoUsado,
        BigDecimal fc,
        String medidaCaseira,
        BigDecimal pb,
        BigDecimal pl
) {}
