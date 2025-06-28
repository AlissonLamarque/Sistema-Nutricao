package com.example.sistemanutricao.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.sistemanutricao.record.UsuarioDTO.GetUsuarioDTO;
import com.example.sistemanutricao.security.UsuarioSecurity;
import com.example.sistemanutricao.service.UsuarioService;

@ControllerAdvice
public class UsernameController {

    private final UsuarioService usuarioService;

    public UsernameController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("Username")
    public String getUsername(@AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        return usuarioPrincipal != null ? usuarioPrincipal.getUsername() : null;
    }

    @ModelAttribute("usuarioLogado")
    public GetUsuarioDTO getUsuarioLogado(@AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        if (usuarioPrincipal != null) {
            return usuarioService.findById(usuarioPrincipal.getId());
        }
        return null;
    }
}