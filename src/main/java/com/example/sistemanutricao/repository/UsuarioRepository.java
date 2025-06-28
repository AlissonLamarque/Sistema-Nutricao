package com.example.sistemanutricao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sistemanutricao.model.Cargo;
import com.example.sistemanutricao.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);
    
    List<Usuario> findByCargo(Cargo cargo);
}
