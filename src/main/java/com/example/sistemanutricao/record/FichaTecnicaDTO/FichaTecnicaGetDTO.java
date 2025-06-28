package com.example.sistemanutricao.record.FichaTecnicaDTO;

import com.example.sistemanutricao.model.Status;
import com.example.sistemanutricao.model.StatusCriacao;
import com.example.sistemanutricao.record.IngredientesPorFichaDTO.IngredientePorFichaGetDTO;
import com.example.sistemanutricao.record.PerfilNutricionalDTO.PerfilNutricionalGetDTO;
import com.example.sistemanutricao.record.PreparacaoDTO.PreparacaoGetDTO;

import java.math.BigDecimal;
import java.util.List;

public record FichaTecnicaGetDTO(
        Long id,
        BigDecimal custoPerCapita,
        BigDecimal custoTotal,
        String medidaCaseira,
        Integer numeroPorcoes,
        BigDecimal pesoPorcao,
        Status status,
        StatusCriacao statusCriacao,
        PreparacaoGetDTO preparacao,
        List<IngredientePorFichaGetDTO> ingredientes,
        PerfilNutricionalGetDTO perfilNutricional
) {}
