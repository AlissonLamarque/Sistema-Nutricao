package com.example.sistemanutricao.repository;

import com.example.sistemanutricao.model.Refeicao;
import com.example.sistemanutricao.model.Status;
import com.example.sistemanutricao.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);
}
