package com.example.sistemanutricao.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.sistemanutricao.model.Usuario;
import com.example.sistemanutricao.repository.UsuarioRepository;
import com.example.sistemanutricao.security.UsuarioSecurity;
import com.example.sistemanutricao.security.UsuarioSemCargoException;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDetailsService.class);
    private final UsuarioRepository repo;

    public UsuarioDetailsService(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        
        logger.info("Tentando carregar usuário: {}", username);
        
        Usuario user = repo.findByUsername(username)
                .orElseThrow(() -> {
                    logger.info("Usuário não encontrado: {}", username);
                    return new UsernameNotFoundException("Usuário não encontrado");
                });
        
        logger.info("Usuário encontrado: {}, cargo: {}", username, user.getCargo());
        
        // Verificar se o usuário tem cargo definido
        if (user.getCargo() == null) {
            logger.info("Usuário sem cargo definido: {}", username);
            throw new UsuarioSemCargoException("Usuário sem cargo definido");
        }
        
        logger.info("Usuário carregado com sucesso: {}", username);
        return new UsuarioSecurity(user);
    }
}
