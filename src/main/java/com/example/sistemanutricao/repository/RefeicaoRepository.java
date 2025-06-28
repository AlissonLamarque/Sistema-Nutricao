package com.example.sistemanutricao.repository;

import com.example.sistemanutricao.model.FichaTecnica;
import com.example.sistemanutricao.model.Refeicao;
import com.example.sistemanutricao.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefeicaoRepository extends JpaRepository<Refeicao, Long> {

    List<Refeicao> findByNomeAndStatusAndNutricionistaId(String nome, Status status, Long nutricionistaId);

    List<Refeicao> findByNomeAndStatusAndNutricionistaEstabelecimentoId(String nome, Status status, Long estabelecimentoId);

    List<Refeicao> findByStatusAndNutricionistaId(Status status, Long nutricionistaId);

    List<Refeicao> findByStatusAndNutricionistaEstabelecimentoId(Status status, Long estabelecimentoId);
}