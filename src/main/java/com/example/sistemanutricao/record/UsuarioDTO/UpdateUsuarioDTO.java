package com.example.sistemanutricao.record.UsuarioDTO;

public record UpdateUsuarioDTO(
        String username,
        String email,
        String novaSenha,
        String confirmarNovaSenha,
        String senhaAtual,
        String caminhoImagem
) {}