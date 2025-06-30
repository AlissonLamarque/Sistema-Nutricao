package com.example.sistemanutricao.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      AuthenticationException exception) 
            throws IOException, ServletException {
        
        logger.info("Falha na autenticação: {}", exception.getClass().getSimpleName());
        logger.info("Mensagem da exceção: {}", exception.getMessage());
        
        String redirectUrl = "/?error";
        
        // Se a exceção for DisabledException (usuário inativo) ou UsuarioSemCargoException (sem cargo), 
        // redireciona para acesso negado
        if (exception instanceof DisabledException) {
            logger.info("Usuário inativo detectado, redirecionando para acesso negado");
            redirectUrl = "/acesso-negado";
        } else if (exception instanceof UsuarioSemCargoException) {
            logger.info("Usuário sem cargo detectado, redirecionando para acesso negado");
            redirectUrl = "/acesso-negado";
        } else {
            logger.info("Outro tipo de erro de autenticação, redirecionando para login com erro");
        }
        
        logger.info("Redirecionando para: {}", redirectUrl);
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
} 