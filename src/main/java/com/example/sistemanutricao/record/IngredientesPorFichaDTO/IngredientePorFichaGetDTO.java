package com.example.sistemanutricao.record.IngredientesPorFichaDTO;

import java.math.BigDecimal;

import com.example.sistemanutricao.record.IngredienteDTO.IngredienteGetDTO;

public record IngredientePorFichaGetDTO(
        Long id,
        IngredienteGetDTO ingrediente,
        BigDecimal custoKg,
        BigDecimal custoUsado,
        BigDecimal fc,
        String medidaCaseira,
        BigDecimal pb,
        BigDecimal pl,
        BigDecimal ptnCalculado,
        BigDecimal choCalculado,
        BigDecimal lipCalculado,
        BigDecimal sodioCalculado,
        BigDecimal gorduraSaturadaCalculada
) {}
