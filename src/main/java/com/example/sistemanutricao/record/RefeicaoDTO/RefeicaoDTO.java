package com.example.sistemanutricao.record.RefeicaoDTO;

import com.example.sistemanutricao.model.Status;

import java.util.List;

public record RefeicaoDTO(
    Long id,
    String nome,
    String kcalTotal,
    Status status,
    List<Long> fichasTecnicasIds
)
{}
