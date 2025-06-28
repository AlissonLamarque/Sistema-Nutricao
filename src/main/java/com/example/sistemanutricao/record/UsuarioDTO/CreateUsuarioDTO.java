package com.example.sistemanutricao.record.UsuarioDTO;

import com.example.sistemanutricao.model.Cargo;

public record CreateUsuarioDTO(
        String username,
        String email,
        String senha
) {
}
