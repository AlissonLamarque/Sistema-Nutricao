package com.example.sistemanutricao.record.FichaTecnicaDTO;

import com.example.sistemanutricao.model.Status;
import com.example.sistemanutricao.model.StatusCriacao;
import com.example.sistemanutricao.record.IngredientesPorFichaDTO.IngredientePorFichaDTO;
import com.example.sistemanutricao.record.PerfilNutricionalDTO.PerfilNutricionalCreateDTO;
import com.example.sistemanutricao.record.PreparacaoDTO.PreparacaoCreateDTO;

import java.math.BigDecimal;
import java.util.List;

public record FichaTecnicaCreateDTO(
        Long id,
        BigDecimal custoPerCapita,
        BigDecimal custoTotal,
        String medidaCaseira,
        Integer numeroPorcoes,
        BigDecimal pesoPorcao,
        Status status,
        StatusCriacao statusCriacao,
        Long nutricionistaId,
        Long preparacaoId,
        PreparacaoCreateDTO preparacao,
        List<IngredientePorFichaDTO> ingredientes,
        PerfilNutricionalCreateDTO perfilNutricional
) {}
