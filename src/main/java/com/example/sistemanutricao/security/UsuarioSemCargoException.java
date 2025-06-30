package com.example.sistemanutricao.security;

import org.springframework.security.core.AuthenticationException;

public class UsuarioSemCargoException extends AuthenticationException {
    
    public UsuarioSemCargoException(String msg) {
        super(msg);
    }
    
    public UsuarioSemCargoException(String msg, Throwable cause) {
        super(msg, cause);
    }
} 