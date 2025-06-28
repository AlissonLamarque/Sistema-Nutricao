package com.example.sistemanutricao.record.IngredientesPorFichaDTO;

import java.math.BigDecimal;

import com.example.sistemanutricao.record.FichaTecnicaDTO.FichaTecnicaCreateDTO;
import com.example.sistemanutricao.record.IngredienteDTO.IngredienteCreateDTO;

public record IngredientePorFichaDTO(
        Long id,
        Long ingredienteId,
        IngredienteCreateDTO ingrediente,
        BigDecimal custoKg,
        BigDecimal custoUsado,
        BigDecimal fc,
        String medidaCaseira,
        BigDecimal pb,
        BigDecimal pl,
        FichaTecnicaCreateDTO fichaTecnica
) {}
