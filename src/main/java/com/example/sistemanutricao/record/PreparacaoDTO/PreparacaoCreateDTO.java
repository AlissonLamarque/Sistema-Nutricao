package com.example.sistemanutricao.record.PreparacaoDTO;

import com.example.sistemanutricao.model.Categoria;
import java.math.BigDecimal;

public record PreparacaoCreateDTO(
        Long id,
        String nome,
        Integer numero,
        String tempoPreparo,
        String equipamentos,
        String modoPreparo,
        BigDecimal porcentAgua,
        BigDecimal qntdAgua,
        BigDecimal fcc,
        BigDecimal rendimento,
        Categoria categoria
) {}

