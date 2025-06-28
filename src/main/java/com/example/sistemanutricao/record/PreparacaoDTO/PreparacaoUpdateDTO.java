package com.example.sistemanutricao.record.PreparacaoDTO;

import java.math.BigDecimal;

import com.example.sistemanutricao.model.Categoria;

public record PreparacaoUpdateDTO(
        Long id,
        String nome,
        Integer  numero,
        String tempoPreparo,
        String equipamentos,
        String modoPreparo,
        BigDecimal porcentAgua,
        BigDecimal qntdAgua,
        BigDecimal fcc,
        BigDecimal rendimento,
        Categoria categoria
) {}
