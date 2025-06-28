package com.example.sistemanutricao.record.IngredienteDTO;

import com.example.sistemanutricao.model.Status;

import java.math.BigDecimal;

public record IngredienteCreateDTO(
        Long id,
        String nome,
        BigDecimal ptn,
        BigDecimal cho,
        BigDecimal lip,
        BigDecimal sodio,
        Status status,
        BigDecimal gorduraSaturada,
        Long usuarioId
) {}
