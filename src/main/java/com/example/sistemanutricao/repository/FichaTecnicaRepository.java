package com.example.sistemanutricao.repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.sistemanutricao.model.Categoria;
import com.example.sistemanutricao.model.FichaTecnica;
import com.example.sistemanutricao.model.Status;
import com.example.sistemanutricao.model.StatusCriacao;

public interface FichaTecnicaRepository extends JpaRepository<FichaTecnica, Long> {

    //NutricionistaController
    List<FichaTecnica> findByStatusAndNutricionista_IdAndStatusCriacao(
            Status status, Long nutricionistaId, StatusCriacao statusCriacao);

    List<FichaTecnica> findByStatusAndNutricionista_Id(
            Status status, Long nutricionistaId);

    List<FichaTecnica> findByStatusCriacaoAndNutricionista_Id(
            StatusCriacao statusCriacao, Long nutricionistaId);

    List<FichaTecnica> findByStatusAndNutricionistaIdAndStatusCriacaoAndCustoPerCapita(
            Status status, Long nutricionistaId, StatusCriacao statusCriacao, BigDecimal custoPerCapita);

    List<FichaTecnica> findByStatusAndNutricionistaIdAndStatusCriacaoAndCustoTotal(
            Status status, Long nutricionistaId, StatusCriacao statusCriacao, BigDecimal custoTotal);

    List<FichaTecnica> findByStatusAndNutricionistaIdAndStatusCriacaoAndPreparacaoNome(
            Status status, Long nutricionistaId, StatusCriacao statusCriacao, String nome);

    List<FichaTecnica> findByStatusAndNutricionistaIdAndStatusCriacaoAndPreparacaoRendimento(
            Status status, Long nutricionistaId, StatusCriacao statusCriacao, BigDecimal rendimento);

    List<FichaTecnica> findByStatusAndNutricionistaIdAndStatusCriacaoAndPreparacaoNumero(
            Status status, Long nutricionistaId, StatusCriacao statusCriacao, Integer numero);

    List<FichaTecnica> findByStatusAndNutricionistaIdAndStatusCriacaoAndPerfilNutricionalVtc(
            Status status, Long nutricionistaId, StatusCriacao statusCriacao, BigDecimal vtc);

    List<FichaTecnica> findByStatusAndNutricionistaIdAndStatusCriacaoAndPerfilNutricionalGramasPTN(
            Status status, Long nutricionistaId, StatusCriacao statusCriacao, BigDecimal gramasPTN);

    List<FichaTecnica> findByStatusAndNutricionistaIdAndStatusCriacaoAndPerfilNutricionalGramasCHO(
            Status status, Long nutricionistaId, StatusCriacao statusCriacao, BigDecimal gramasCHO);

    List<FichaTecnica> findByStatusAndNutricionistaIdAndStatusCriacaoAndPerfilNutricionalGramasLIP(
            Status status, Long nutricionistaId, StatusCriacao statusCriacao, BigDecimal gramasLIP);

    List<FichaTecnica> findByStatusAndNutricionistaIdAndStatusCriacaoAndPerfilNutricionalGramasSodio(
            Status status, Long nutricionistaId, StatusCriacao statusCriacao, BigDecimal gramasSodio);

    List<FichaTecnica> findByStatusAndNutricionistaIdAndStatusCriacaoAndPerfilNutricionalGramasSaturada(
            Status status, Long nutricionistaId, StatusCriacao statusCriacao, BigDecimal gramasSaturada);


    //ProducaoController
    List<FichaTecnica> findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacao(
            Status status, Long estabelecimentoId, StatusCriacao statusCriacao);

    List<FichaTecnica> findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndCustoPerCapita(
            Status status, Long estabelecimentoId, StatusCriacao statusCriacao, BigDecimal custoPerCapita);

    List<FichaTecnica> findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndCustoTotal(
            Status status, Long estabelecimentoId, StatusCriacao statusCriacao, BigDecimal custoTotal);

    List<FichaTecnica> findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPreparacaoNome(
            Status status, Long estabelecimentoId, StatusCriacao statusCriacao, String nome);

    List<FichaTecnica> findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPreparacaoRendimento(
            Status status, Long estabelecimentoId, StatusCriacao statusCriacao, BigDecimal rendimento);

    List<FichaTecnica> findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPreparacaoNumero(
            Status status, Long estabelecimentoId, StatusCriacao statusCriacao, Integer numero);

    List<FichaTecnica> findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPerfilNutricionalVtc(
            Status status, Long estabelecimentoId, StatusCriacao statusCriacao, BigDecimal vtc);

    List<FichaTecnica> findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPerfilNutricionalGramasPTN(
            Status status, Long estabelecimentoId, StatusCriacao statusCriacao, BigDecimal gramasPTN);

    List<FichaTecnica> findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPerfilNutricionalGramasCHO(
            Status status, Long estabelecimentoId, StatusCriacao statusCriacao, BigDecimal gramasCHO);

    List<FichaTecnica> findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPerfilNutricionalGramasLIP(
            Status status, Long estabelecimentoId, StatusCriacao statusCriacao, BigDecimal gramasLIP);

    List<FichaTecnica> findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPerfilNutricionalGramasSodio(
            Status status, Long estabelecimentoId, StatusCriacao statusCriacao, BigDecimal gramasSodio);

    List<FichaTecnica> findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPerfilNutricionalGramasSaturada(
            Status status, Long estabelecimentoId, StatusCriacao statusCriacao, BigDecimal gramasSaturada);



    //Tem que arrumar!!
    @Query("SELECT f FROM FichaTecnica f WHERE f.preparacao.categoria = :categoria")
    List<FichaTecnica> findByPreparacaoCategoria(@Param("categoria") Categoria categoria);

    default List<FichaTecnica> findByPreparacaoCategoriaNome(String nomeCategoria) {
        Categoria categoria = Arrays.stream(Categoria.values())
                .filter(c -> c.getNome().equalsIgnoreCase(nomeCategoria))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Categoria inválida: " + nomeCategoria));

        return findByPreparacaoCategoria(categoria);
    }

    // Método para listar resumo de fichas (usado no formulário de refeição)
    List<FichaTecnica> findByStatusAndStatusCriacao(Status status, StatusCriacao statusCriacao);
}
