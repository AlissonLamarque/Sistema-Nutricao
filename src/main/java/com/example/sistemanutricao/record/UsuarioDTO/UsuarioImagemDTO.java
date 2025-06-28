package com.example.sistemanutricao.record.UsuarioDTO;

import org.springframework.web.multipart.MultipartFile;

public record UsuarioImagemDTO(
        Long usuarioId,
        MultipartFile arquivoImagem
) {}