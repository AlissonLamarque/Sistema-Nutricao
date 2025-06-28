package com.example.sistemanutricao.repository;

import com.example.sistemanutricao.model.FichaTecnica;
import com.example.sistemanutricao.model.Ingrediente;
import com.example.sistemanutricao.model.Status;
import com.example.sistemanutricao.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

    List<Ingrediente> findByNomeAndStatusAndUsuario_Id(String nome, Status status, Long UsuarioId);

    List<Ingrediente> findByPtnAndStatusAndUsuario_Id(BigDecimal ptn, Status status, Long UsuarioId);

    List<Ingrediente> findByChoAndStatusAndUsuario_Id(BigDecimal cho, Status status, Long UsuarioId);

    List<Ingrediente> findByLipAndStatusAndUsuario_Id(BigDecimal lip, Status status, Long UsuarioId);

    List<Ingrediente> findBySodioAndStatusAndUsuario_Id(BigDecimal sodio, Status status, Long UsuarioId);

    List<Ingrediente> findByGorduraSaturadaAndStatusAndUsuario_Id(BigDecimal gorduraSaturada, Status status, Long UsuarioId);

    List<Ingrediente> findByUsuario(Usuario usuario);

    List<Ingrediente> findByStatusAndUsuario(Status status, Usuario usuario);

    List<Ingrediente> findByStatusAndUsuario_Id(Status status, Long usuarioId);


    @Query("SELECT i FROM Ingrediente i WHERE i.status = :status AND i.usuario.id IN :usuariosIds")
    List<Ingrediente> findByStatusAndUsuarioIdIn(@Param("status") Status status,
                                                 @Param("usuariosIds") List<Long> usuariosIds);

    List<Ingrediente> findByPtnLessThanAndStatusAndUsuario_Id(BigDecimal ptn, Status status, Long usuarioId);
    List<Ingrediente> findByPtnBetweenAndStatusAndUsuario_Id(BigDecimal de, BigDecimal ate, Status status, Long usuarioId);
    List<Ingrediente> findByPtnGreaterThanAndStatusAndUsuario_Id(BigDecimal ptn, Status status, Long usuarioId);

    List<Ingrediente> findByChoLessThanAndStatusAndUsuario_Id(BigDecimal cho, Status status, Long usuarioId);
    List<Ingrediente> findByChoBetweenAndStatusAndUsuario_Id(BigDecimal de, BigDecimal ate, Status status, Long usuarioId);
    List<Ingrediente> findByChoGreaterThanAndStatusAndUsuario_Id(BigDecimal cho, Status status, Long usuarioId);

    // GORDURA TOTAL (lip)
    List<Ingrediente> findByLipLessThanAndStatusAndUsuario_Id(BigDecimal lip, Status status, Long usuarioId);
    List<Ingrediente> findByLipBetweenAndStatusAndUsuario_Id(BigDecimal de, BigDecimal ate, Status status, Long usuarioId);
    List<Ingrediente> findByLipGreaterThanAndStatusAndUsuario_Id(BigDecimal lip, Status status, Long usuarioId);

    // GORDURA SATURADA
    List<Ingrediente> findByGorduraSaturadaLessThanAndStatusAndUsuario_Id(BigDecimal gorduraSaturada, Status status, Long usuarioId);
    List<Ingrediente> findByGorduraSaturadaBetweenAndStatusAndUsuario_Id(BigDecimal de, BigDecimal ate, Status status, Long usuarioId);
    List<Ingrediente> findByGorduraSaturadaGreaterThanAndStatusAndUsuario_Id(BigDecimal gorduraSaturada, Status status, Long usuarioId);

    // SÓDIO
    List<Ingrediente> findBySodioLessThanAndStatusAndUsuario_Id(BigDecimal sodio, Status status, Long usuarioId);
    List<Ingrediente> findBySodioBetweenAndStatusAndUsuario_Id(BigDecimal de, BigDecimal ate, Status status, Long usuarioId);
    List<Ingrediente> findBySodioGreaterThanAndStatusAndUsuario_Id(BigDecimal sodio, Status status, Long usuarioId);


}