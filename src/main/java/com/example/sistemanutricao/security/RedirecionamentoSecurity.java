package com.example.sistemanutricao.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class RedirecionamentoSecurity implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/";

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            redirectUrl = "/admin";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_NUTRICIONISTA"))) {
            redirectUrl = "/nutricionista";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_PRODUCAO"))) {
            redirectUrl = "/producao";
        }

        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
