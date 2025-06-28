package com.example.sistemanutricao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.sistemanutricao.model.Cargo;
import com.example.sistemanutricao.model.Usuario;
import com.example.sistemanutricao.repository.UsuarioRepository;

@Component
public class AdminInitializationService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminInitializationService.class);
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializationService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Verificando se existe usuário admin no sistema...");
        
        // Verifica se já existe algum usuário com cargo ADMIN
        var usuariosAdmin = usuarioRepository.findByCargo(Cargo.ADMIN);
        
        if (usuariosAdmin.isEmpty()) {
            logger.info("Nenhum usuário admin encontrado. Criando usuário admin padrão...");
            criarUsuarioAdminPadrao();
        } else {
            logger.info("Usuário(s) admin já existente(s) no sistema. Nenhuma ação necessária.");
        }
    }

    private void criarUsuarioAdminPadrao() {
        try {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            admin.setSenha(passwordEncoder.encode("1234567"));
            admin.setCargo(Cargo.ADMIN);
            admin.setAtivo(true);
            
            usuarioRepository.save(admin);
            logger.info("Usuário admin padrão criado com sucesso!");
            logger.info("Username: admin");
            logger.info("Email: admin@gmail.com");
            logger.info("Senha: 1234567");
            
        } catch (Exception e) {
            logger.error("Erro ao criar usuário admin padrão: {}", e.getMessage(), e);
        }
    }
} 