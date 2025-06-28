package com.example.sistemanutricao.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.sistemanutricao.model.FichaTecnica;
import com.example.sistemanutricao.model.PerfilNutricional;
import com.example.sistemanutricao.model.Preparacao;
import com.example.sistemanutricao.model.Status;
import com.example.sistemanutricao.model.StatusCriacao;
import com.example.sistemanutricao.model.Usuario;
import com.example.sistemanutricao.record.FichaTecnicaDTO.FichaTecnicaComTagDTO;
import com.example.sistemanutricao.record.FichaTecnicaDTO.FichaTecnicaCreateDTO;
import com.example.sistemanutricao.record.FichaTecnicaDTO.FichaTecnicaGetDTO;
import com.example.sistemanutricao.record.FichaTecnicaDTO.FichaTecnicaUpdateDTO;
import com.example.sistemanutricao.record.IngredientesPorFichaDTO.IngredientePorFichaDTO;
import com.example.sistemanutricao.record.IngredientesPorFichaDTO.IngredientePorFichaGetDTO;
import com.example.sistemanutricao.record.PerfilNutricionalDTO.PerfilNutricionalGetDTO;
import com.example.sistemanutricao.record.PreparacaoDTO.PreparacaoGetDTO;
import com.example.sistemanutricao.record.RefeicaoDTO.FichaTecnicaRefeicaoDTO;
import com.example.sistemanutricao.repository.FichaTecnicaRepository;
import com.example.sistemanutricao.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FichaTecnicaService {

    private final FichaTecnicaRepository fichaRepository;
    private final PreparacaoService preparacaoService;
    private final PerfilNutricionalService perfilService;
    private final IngredientesPorFichaService ipfService;
    private final UsuarioRepository usuarioRepository;
    private final IngredientesPorFichaService ingredientesPorFichaService;

    public FichaTecnicaService(FichaTecnicaRepository fichaRepository,
                               PreparacaoService preparacaoService,
                               PerfilNutricionalService perfilService,
                               IngredientesPorFichaService ipfService,
                               UsuarioRepository usuarioRepository,
                               IngredientesPorFichaService ingredientesPorFichaService) {
        this.fichaRepository = fichaRepository;
        this.preparacaoService = preparacaoService;
        this.perfilService = perfilService;
        this.ipfService = ipfService;
        this.usuarioRepository = usuarioRepository;
        this.ingredientesPorFichaService = ingredientesPorFichaService;
    }

    public FichaTecnicaGetDTO create(
            FichaTecnicaCreateDTO dto,
            Long nutricionistaId
    ) {
        PreparacaoGetDTO preparacaoDTO = preparacaoService.create(dto.preparacao());
        PerfilNutricionalGetDTO perfilDTO = perfilService.create(dto.perfilNutricional());

        FichaTecnica ficha = new FichaTecnica();
        ficha.setCustoPerCapita(dto.custoPerCapita());
        ficha.setCustoTotal(dto.custoTotal());
        ficha.setNumeroPorcoes(dto.numeroPorcoes());
        ficha.setPesoPorcao(dto.pesoPorcao());
        ficha.setMedidaCaseira(dto.medidaCaseira());
        ficha.setStatus(dto.status());
        ficha.setStatusCriacao(dto.statusCriacao());
        ficha.setPreparacao(new Preparacao(preparacaoDTO.id()));
        ficha.setPerfilNutricional(new PerfilNutricional(perfilDTO.id()));

        Usuario nutricionista = usuarioRepository.findById(nutricionistaId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        ficha.setNutricionista(nutricionista);

        FichaTecnica fichaSalva = fichaRepository.save(ficha);

        List<IngredientePorFichaGetDTO> ingredientesDTOs =
                ipfService.create(dto.ingredientes(), fichaSalva, perfilDTO.id());

        return convertToDto(fichaSalva, ingredientesDTOs);
    }

    public FichaTecnicaGetDTO update(Long id, FichaTecnicaUpdateDTO dto) {
        FichaTecnica fichaExistente = fichaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ficha Técnica não encontrada com ID: " + id));

        Preparacao preparacaoAtualizada = preparacaoService.update(dto.preparacao());
        PerfilNutricional perfilAtualizado = perfilService.update(
                fichaExistente.getPerfilNutricional().getId(),
                dto.perfilNutricional());

        fichaExistente.setCustoPerCapita(dto.custoPerCapita());
        fichaExistente.setCustoTotal(dto.custoTotal());
        fichaExistente.setNumeroPorcoes(dto.numeroPorcoes());
        fichaExistente.setPesoPorcao(dto.pesoPorcao());
        fichaExistente.setMedidaCaseira(dto.medidaCaseira());
        fichaExistente.setStatus(dto.status());
        fichaExistente.setStatusCriacao(dto.statusCriacao());
        fichaExistente.setPreparacao(preparacaoAtualizada);
        fichaExistente.setPerfilNutricional(perfilAtualizado);

        // 1. Pegue os ids dos ingredientes que vieram do formulário
        Set<Long> idsNoForm = dto.ingredientes().stream()
            .filter(ing -> ing != null && ing.id() != null)
            .map(IngredientePorFichaDTO::id)
            .collect(Collectors.toSet());

        // 2. Pegue os ids dos ingredientes que já estavam na ficha
        List<IngredientePorFichaGetDTO> ingredientesAtuais = ipfService.listarIngredientesPorFichaId(fichaExistente.getId());
        Set<Long> idsAtuais = ingredientesAtuais.stream()
            .map(IngredientePorFichaGetDTO::id)
            .collect(Collectors.toSet());

        // 3. Descubra quais ids sumiram (precisam ser removidos)
        Set<Long> idsParaRemover = new HashSet<>(idsAtuais);
        idsParaRemover.removeAll(idsNoForm);

        // 4. Remova do banco
        for (Long idRemover : idsParaRemover) {
            ipfService.delete(idRemover);
        }

        FichaTecnica fichaAtualizada = fichaRepository.save(fichaExistente);

        List<IngredientePorFichaGetDTO> ingredientesAtualizados = new ArrayList<>();

        for (IngredientePorFichaDTO ingredienteDTO : dto.ingredientes()) {
            if (ingredienteDTO != null && ingredienteDTO.id() != null) {
                ingredientesAtualizados.add(ipfService.update(ingredienteDTO, ingredienteDTO.id()));
            }
        }

        List<IngredientePorFichaDTO> novosIngredientes = dto.ingredientes().stream()
                .filter(ing -> ing != null && ing.id() == null)
                .toList();

        if (!novosIngredientes.isEmpty()) {
            ingredientesAtualizados.addAll(ipfService.create(novosIngredientes, fichaAtualizada, perfilAtualizado.getId()));
        }

        return convertToDto(fichaAtualizada, ingredientesAtualizados);
    }

    public List<FichaTecnicaRefeicaoDTO> listarResumo() {
        return fichaRepository.findByStatusAndStatusCriacao(Status.ATIVA, StatusCriacao.COMPLETA).stream()
                .map(f -> new FichaTecnicaRefeicaoDTO(
                        f.getId(),
                        f.getPreparacao().getNome(),
                        f.getPerfilNutricional().getVtc()
                ))
                .collect(Collectors.toList());
    }

    public void atualizaStatus(Long id) {
        FichaTecnica ficha = fichaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ficha não encontrada"));

        ficha.setStatus(
                ficha.getStatus() == Status.ATIVA ?
                        Status.INATIVA :
                        Status.ATIVA
        );

        fichaRepository.save(ficha);
    }

    public FichaTecnicaGetDTO getFichaById(Long id) {
        FichaTecnica ficha = fichaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ficha técnica não encontrada com ID: " + id));
        return convertToDto(
                ficha,
                ipfService
                        .listarIngredientesPorFichaId(ficha.getId())
        );
    }

    //Consultas Base do Nutricionista
    public List<FichaTecnicaGetDTO> buscarPorStatusENutricionista(Status status, Long nutricionistaId) {
        return fichaRepository.findByStatusAndNutricionista_Id(status, nutricionistaId)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorStatusCriacaoENutricionista(StatusCriacao statusCriacao, Long nutricionistaId) {
        return fichaRepository.findByStatusCriacaoAndNutricionista_Id(
                        statusCriacao, nutricionistaId)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorStatusENutricionistaEStatusCriacao(Status status, Long nutricionistaId, StatusCriacao statusCriacao) {
        return fichaRepository.findByStatusAndNutricionista_IdAndStatusCriacao(status, nutricionistaId, statusCriacao)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    //Consulta Base da Produção
    public List<FichaTecnicaGetDTO> buscarPorStatusEEstabelecimento(Status status, Long estabelecimentoId, StatusCriacao statusCriacao) {
        return fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacao(status, estabelecimentoId, statusCriacao)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    //Consultas Personalizadas do Nutricionista
    public List<FichaTecnicaGetDTO> buscarPorCustoPerCapitaENutricionista(BigDecimal custoPerCapita, Long nutricionistaId) {
        return fichaRepository.findByStatusAndNutricionistaIdAndStatusCriacaoAndCustoPerCapita(
                        Status.ATIVA, nutricionistaId, StatusCriacao.COMPLETA, custoPerCapita)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorCustoTotalENutricionista(BigDecimal custoTotal, Long nutricionistaId) {
        return fichaRepository.findByStatusAndNutricionistaIdAndStatusCriacaoAndCustoTotal(
                        Status.ATIVA, nutricionistaId, StatusCriacao.COMPLETA, custoTotal)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorNomePreparacaoENutricionista(String nome, Long nutricionistaId) {
        return fichaRepository.findByStatusAndNutricionistaIdAndStatusCriacaoAndPreparacaoNome(
                        Status.ATIVA, nutricionistaId, StatusCriacao.COMPLETA, nome)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorRendimentoPreparacaoENutricionista(BigDecimal rendimento, Long nutricionistaId) {
        return fichaRepository.findByStatusAndNutricionistaIdAndStatusCriacaoAndPreparacaoRendimento(
                        Status.ATIVA, nutricionistaId, StatusCriacao.COMPLETA, rendimento)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorNumeroPreparacaoENutricionista(Integer numero, Long nutricionistaId) {
        return fichaRepository.findByStatusAndNutricionistaIdAndStatusCriacaoAndPreparacaoNumero(
                        Status.ATIVA, nutricionistaId, StatusCriacao.COMPLETA, numero)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorVtcPerfilNutricionalENutricionista(BigDecimal vtc, Long nutricionistaId) {
        return fichaRepository.findByStatusAndNutricionistaIdAndStatusCriacaoAndPerfilNutricionalVtc(
                        Status.ATIVA, nutricionistaId, StatusCriacao.COMPLETA, vtc)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorGramasPTNPerfilNutricionalENutricionista(BigDecimal gramasPTN, Long nutricionistaId) {
        return fichaRepository.findByStatusAndNutricionistaIdAndStatusCriacaoAndPerfilNutricionalGramasPTN(
                        Status.ATIVA, nutricionistaId, StatusCriacao.COMPLETA, gramasPTN)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorGramasCHOPerfilNutricionalENutricionista(BigDecimal gramasCHO, Long nutricionistaId) {
        return fichaRepository.findByStatusAndNutricionistaIdAndStatusCriacaoAndPerfilNutricionalGramasCHO(
                        Status.ATIVA, nutricionistaId, StatusCriacao.COMPLETA, gramasCHO)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorGramasLIPPerfilNutricionalENutricionista(BigDecimal gramasLIP, Long nutricionistaId) {
        return fichaRepository.findByStatusAndNutricionistaIdAndStatusCriacaoAndPerfilNutricionalGramasLIP(
                        Status.ATIVA, nutricionistaId, StatusCriacao.COMPLETA, gramasLIP)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorGramasSodioPerfilNutricionalENutricionista(BigDecimal gramasSodio, Long nutricionistaId) {
        return fichaRepository.findByStatusAndNutricionistaIdAndStatusCriacaoAndPerfilNutricionalGramasSodio(
                        Status.ATIVA, nutricionistaId, StatusCriacao.COMPLETA, gramasSodio)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorGramasSaturadaPerfilNutricionalENutricionista(BigDecimal gramasSaturada, Long nutricionistaId) {
        return fichaRepository.findByStatusAndNutricionistaIdAndStatusCriacaoAndPerfilNutricionalGramasSaturada(
                        Status.ATIVA, nutricionistaId, StatusCriacao.COMPLETA, gramasSaturada)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    //Consultas Personalizadas da Produção
    public List<FichaTecnicaGetDTO> buscarPorCustPerCapitaEEstabelecimento(BigDecimal custoPerCapita, Long estabelecimentoId) {
        return fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndCustoPerCapita(
                        Status.ATIVA, estabelecimentoId, StatusCriacao.COMPLETA, custoPerCapita)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorCustoTotalEEstabelecimento(BigDecimal custoTotal, Long estabelecimentoId) {
        return fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndCustoTotal(
                        Status.ATIVA, estabelecimentoId, StatusCriacao.COMPLETA, custoTotal)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorNomePreparacaoEEstabelecimento(String nome, Long estabelecimentoId) {
        return fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPreparacaoNome(
                        Status.ATIVA, estabelecimentoId, StatusCriacao.COMPLETA, nome)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorRendimentoPreparacaoEEstabelecimento(BigDecimal rendimento, Long estabelecimentoId) {
        return fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPreparacaoRendimento(
                        Status.ATIVA, estabelecimentoId, StatusCriacao.COMPLETA, rendimento)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorNumeroPreparacaoEEstabelecimento(Integer numero, Long estabelecimentoId) {
        return fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPreparacaoNumero(
                        Status.ATIVA, estabelecimentoId, StatusCriacao.COMPLETA, numero)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorVtcPerfilNutricionalEEstabelecimento(BigDecimal vtc, Long estabelecimentoId) {
        return fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPerfilNutricionalVtc(
                        Status.ATIVA, estabelecimentoId, StatusCriacao.COMPLETA, vtc)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorGramasPTNPerfilNutricionalEEstabelecimento(BigDecimal gramasPTN, Long estabelecimentoId) {
        return fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPerfilNutricionalGramasPTN(
                        Status.ATIVA, estabelecimentoId, StatusCriacao.COMPLETA, gramasPTN)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorGramasCHOPerfilNutricionalEEstabelecimento(BigDecimal gramasCHO, Long estabelecimentoId) {
        return fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPerfilNutricionalGramasCHO(
                        Status.ATIVA, estabelecimentoId, StatusCriacao.COMPLETA, gramasCHO)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorGramasLIPPerfilNutricionalEEstabelecimento(BigDecimal gramasLIP, Long estabelecimentoId) {
        return fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPerfilNutricionalGramasLIP(
                        Status.ATIVA, estabelecimentoId, StatusCriacao.COMPLETA, gramasLIP)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorGramasSodioPerfilNutricionalEEstabelecimento(BigDecimal gramasSodio, Long estabelecimentoId) {
        return fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacaoAndPerfilNutricionalGramasSodio(
                        Status.ATIVA, estabelecimentoId, StatusCriacao.COMPLETA, gramasSodio)
                .stream()
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    public List<FichaTecnicaGetDTO> buscarPorGramasSaturadaPerfilNutricionalEEstabelecimento(BigDecimal gramasSaturada, Long estabelecimentoId) {
        return fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacao(Status.ATIVA, estabelecimentoId, StatusCriacao.COMPLETA)
                .stream()
                .filter(ficha -> ficha.getPerfilNutricional().getGramasSaturada().equals(gramasSaturada))
                .map(ficha -> convertToDto(ficha, ipfService.getAllIngredientesPorFicha()))
                .toList();
    }

    // Métodos para pesquisa por tags
    public List<FichaTecnicaComTagDTO> buscarPorTag(String campo, String tag, Long nutricionistaId) {
        try {
            if (campo == null || tag == null || nutricionistaId == null) {
                System.err.println("Invalid parameters: campo=" + campo + ", tag=" + tag + ", nutricionistaId=" + nutricionistaId);
                return new ArrayList<>();
            }
            
            List<FichaTecnica> fichas = fichaRepository.findByStatusAndNutricionista_IdAndStatusCriacao(Status.ATIVA, nutricionistaId, StatusCriacao.COMPLETA);
            
            if (fichas == null || fichas.isEmpty()) {
                System.out.println("No fichas found for nutricionistaId: " + nutricionistaId);
                return new ArrayList<>();
            }
            
            return fichas.stream()
                    .filter(ficha -> ficha.getPreparacao() != null && ficha.getPerfilNutricional() != null && ficha.getNutricionista() != null)
                    .map(ficha -> {
                        try {
                            return new FichaTecnicaComTagDTO(
                                    ficha.getId(),
                                    ficha.getPreparacao().getNome() != null ? ficha.getPreparacao().getNome() : "",
                                    ficha.getPreparacao().getCategoria() != null ? ficha.getPreparacao().getCategoria().getNome() : "",
                                    ficha.getPreparacao().getNumero() != null ? ficha.getPreparacao().getNumero() : 0,
                                    ficha.getCustoPerCapita(),
                                    ficha.getCustoTotal(),
                                    ficha.getPreparacao().getRendimento(),
                                    ficha.getPerfilNutricional().getVtc(),
                                    ficha.getPerfilNutricional().getGramasPTN(),
                                    ficha.getPerfilNutricional().getGramasCHO(),
                                    ficha.getPerfilNutricional().getGramasLIP(),
                                    ficha.getPerfilNutricional().getGramasSodio(),
                                    ficha.getPerfilNutricional().getGramasSaturada(),
                                    ficha.getStatus(),
                                    ficha.getStatusCriacao(),
                                    ficha.getNutricionista().getId(),
                                    determinarTag(ficha, campo)
                            );
                        } catch (Exception e) {
                            System.err.println("Error creating FichaTecnicaComTagDTO for ficha ID " + ficha.getId() + ": " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(ficha -> ficha != null && ficha.tag().equalsIgnoreCase(tag))
                    .toList();
        } catch (Exception e) {
            System.err.println("Error in buscarPorTag: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<FichaTecnicaComTagDTO> buscarPorTagEstabelecimento(String campo, String tag, Long estabelecimentoId) {
        try {
            if (campo == null || tag == null || estabelecimentoId == null) {
                System.err.println("Invalid parameters: campo=" + campo + ", tag=" + tag + ", estabelecimentoId=" + estabelecimentoId);
                return new ArrayList<>();
            }
            
            List<FichaTecnica> fichas = fichaRepository.findByStatusAndNutricionistaEstabelecimentoIdAndStatusCriacao(Status.ATIVA, estabelecimentoId, StatusCriacao.COMPLETA);
            
            if (fichas == null || fichas.isEmpty()) {
                System.out.println("No fichas found for estabelecimentoId: " + estabelecimentoId);
                return new ArrayList<>();
            }
            
            return fichas.stream()
                    .filter(ficha -> ficha.getPreparacao() != null && ficha.getPerfilNutricional() != null && ficha.getNutricionista() != null)
                    .map(ficha -> {
                        try {
                            return new FichaTecnicaComTagDTO(
                                    ficha.getId(),
                                    ficha.getPreparacao().getNome() != null ? ficha.getPreparacao().getNome() : "",
                                    ficha.getPreparacao().getCategoria() != null ? ficha.getPreparacao().getCategoria().getNome() : "",
                                    ficha.getPreparacao().getNumero() != null ? ficha.getPreparacao().getNumero() : 0,
                                    ficha.getCustoPerCapita(),
                                    ficha.getCustoTotal(),
                                    ficha.getPreparacao().getRendimento(),
                                    ficha.getPerfilNutricional().getVtc(),
                                    ficha.getPerfilNutricional().getGramasPTN(),
                                    ficha.getPerfilNutricional().getGramasCHO(),
                                    ficha.getPerfilNutricional().getGramasLIP(),
                                    ficha.getPerfilNutricional().getGramasSodio(),
                                    ficha.getPerfilNutricional().getGramasSaturada(),
                                    ficha.getStatus(),
                                    ficha.getStatusCriacao(),
                                    ficha.getNutricionista().getId(),
                                    determinarTag(ficha, campo)
                            );
                        } catch (Exception e) {
                            System.err.println("Error creating FichaTecnicaComTagDTO for ficha ID " + ficha.getId() + ": " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(ficha -> ficha != null && ficha.tag().equalsIgnoreCase(tag))
                    .toList();
        } catch (Exception e) {
            System.err.println("Error in buscarPorTagEstabelecimento: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String determinarTag(FichaTecnica ficha, String campo) {
        try {
            if (ficha == null || campo == null) {
                return "Baixa";
            }
            
            BigDecimal valor = obterValorCampo(ficha, campo);
            if (valor == null) return "Baixa";

            // Definir limites para cada campo
            switch (campo.toLowerCase()) {
                case "custoPerCapita":
                    return valor.compareTo(new BigDecimal("5.00")) >= 0 ? "Alta" : 
                           valor.compareTo(new BigDecimal("2.50")) >= 0 ? "Media" : "Baixa";
                case "custoTotal":
                    return valor.compareTo(new BigDecimal("100.00")) >= 0 ? "Alta" : 
                           valor.compareTo(new BigDecimal("50.00")) >= 0 ? "Media" : "Baixa";
                case "rendimento":
                    return valor.compareTo(new BigDecimal("50")) >= 0 ? "Alta" : 
                           valor.compareTo(new BigDecimal("25")) >= 0 ? "Media" : "Baixa";
                case "vtc":
                    return valor.compareTo(new BigDecimal("500")) >= 0 ? "Alta" : 
                           valor.compareTo(new BigDecimal("300")) >= 0 ? "Media" : "Baixa";
                case "gramasPTN":
                    return valor.compareTo(new BigDecimal("20")) >= 0 ? "Alta" : 
                           valor.compareTo(new BigDecimal("10")) >= 0 ? "Media" : "Baixa";
                case "gramasCHO":
                    return valor.compareTo(new BigDecimal("60")) >= 0 ? "Alta" : 
                           valor.compareTo(new BigDecimal("30")) >= 0 ? "Media" : "Baixa";
                case "gramasLIP":
                    return valor.compareTo(new BigDecimal("20")) >= 0 ? "Alta" : 
                           valor.compareTo(new BigDecimal("10")) >= 0 ? "Media" : "Baixa";
                case "gramasSodio":
                    return valor.compareTo(new BigDecimal("1000")) >= 0 ? "Alta" : 
                           valor.compareTo(new BigDecimal("500")) >= 0 ? "Media" : "Baixa";
                case "gramasSaturada":
                    return valor.compareTo(new BigDecimal("10")) >= 0 ? "Alta" : 
                           valor.compareTo(new BigDecimal("5")) >= 0 ? "Media" : "Baixa";
                default:
                    return "Baixa";
            }
        } catch (Exception e) {
            System.err.println("Error in determinarTag: " + e.getMessage());
            return "Baixa";
        }
    }

    private BigDecimal obterValorCampo(FichaTecnica ficha, String campo) {
        switch (campo.toLowerCase()) {
            case "custoPerCapita":
                return ficha.getCustoPerCapita();
            case "custoTotal":
                return ficha.getCustoTotal();
            case "rendimento":
                return ficha.getPreparacao() != null ? ficha.getPreparacao().getRendimento() : null;
            case "vtc":
                return ficha.getPerfilNutricional() != null ? ficha.getPerfilNutricional().getVtc() : null;
            case "gramasPTN":
                return ficha.getPerfilNutricional() != null ? ficha.getPerfilNutricional().getGramasPTN() : null;
            case "gramasCHO":
                return ficha.getPerfilNutricional() != null ? ficha.getPerfilNutricional().getGramasCHO() : null;
            case "gramasLIP":
                return ficha.getPerfilNutricional() != null ? ficha.getPerfilNutricional().getGramasLIP() : null;
            case "gramasSodio":
                return ficha.getPerfilNutricional() != null ? ficha.getPerfilNutricional().getGramasSodio() : null;
            case "gramasSaturada":
                return ficha.getPerfilNutricional() != null ? ficha.getPerfilNutricional().getGramasSaturada() : null;
            default:
                return null;
        }
    }

    //Tem que arrumar!!
    public List<FichaTecnicaGetDTO> buscarPorCategoriaPreparacao(String nome) {
        return fichaRepository.findByPreparacaoCategoriaNome(nome).stream()
                .map(ficha -> convertToDto(
                        ficha,
                        ipfService.getAllIngredientesPorFicha()
                ))
                .toList();
    }

    private FichaTecnicaGetDTO convertToDto(FichaTecnica ficha,
                                                List<IngredientePorFichaGetDTO> ingredientes) {
            return new FichaTecnicaGetDTO(
                    ficha.getId(),
                    ficha.getCustoPerCapita(),
                    ficha.getCustoTotal(),
                    ficha.getMedidaCaseira(),
                    ficha.getNumeroPorcoes(),
                    ficha.getPesoPorcao(),
                    ficha.getStatus(),
                    ficha.getStatusCriacao(),
                    preparacaoService.getPreparacaoById(ficha.getPreparacao().getId()),
                    ingredientes,
                    perfilService.getPerfilNutricionalById(ficha.getPerfilNutricional().getId())
            );
        }
}


