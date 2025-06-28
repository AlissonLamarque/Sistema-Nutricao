package com.example.sistemanutricao.record.FichaTecnicaDTO;

import com.example.sistemanutricao.model.Status;
import com.example.sistemanutricao.model.StatusCriacao;
import com.example.sistemanutricao.record.IngredientesPorFichaDTO.IngredientePorFichaDTO;
import com.example.sistemanutricao.record.PerfilNutricionalDTO.PerfilNutricionalUpdateDTO;
import com.example.sistemanutricao.record.PreparacaoDTO.PreparacaoUpdateDTO;

import java.math.BigDecimal;
import java.util.List;

public record FichaTecnicaUpdateDTO(
        Long id,
        BigDecimal custoPerCapita,
        BigDecimal custoTotal,
        String medidaCaseira,
        Integer numeroPorcoes,
        BigDecimal pesoPorcao,
        Status status,
        StatusCriacao statusCriacao,
        PreparacaoUpdateDTO preparacao,
        List<IngredientePorFichaDTO> ingredientes,
        PerfilNutricionalUpdateDTO perfilNutricional
) {}
