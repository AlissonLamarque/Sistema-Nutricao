package com.example.sistemanutricao.service;

import com.example.sistemanutricao.model.*;
import com.example.sistemanutricao.record.RefeicaoDTO.FichaTecnicaRefeicaoDTO;
import com.example.sistemanutricao.record.RefeicaoDTO.RefeicaoDTO;
import com.example.sistemanutricao.record.RefeicaoDTO.RefeicaoResponseDTO;
import com.example.sistemanutricao.repository.RefeicaoRepository;
import com.example.sistemanutricao.repository.FichasPorRefeicaoRepository;
import com.example.sistemanutricao.repository.FichaTecnicaRepository;
import com.example.sistemanutricao.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RefeicaoService {

    private final RefeicaoRepository refeicaoRepository;
    private final FichaTecnicaRepository fichaTecnicaRepository;
    private final FichasPorRefeicaoRepository fichasPorRefeicaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public RefeicaoService(RefeicaoRepository refeicaoRepository,
                           FichaTecnicaRepository fichaTecnicaRepository,
                           FichasPorRefeicaoRepository fichasPorRefeicaoRepository,
                           UsuarioRepository usuarioRepository) {
        this.refeicaoRepository = refeicaoRepository;
        this.fichaTecnicaRepository = fichaTecnicaRepository;
        this.fichasPorRefeicaoRepository = fichasPorRefeicaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public RefeicaoResponseDTO create(RefeicaoDTO dto, Long nutricionistaId) {
        Refeicao refeicao = new Refeicao();
        refeicao.setNome(dto.nome());
        refeicao.setKcalTotal(dto.kcalTotal());
        refeicao.setStatus(dto.status());

        Usuario nutricionista = usuarioRepository.findById(nutricionistaId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        refeicao.setNutricionista(nutricionista);

        Refeicao savedRefeicao = refeicaoRepository.save(refeicao);

        if (dto.fichasTecnicasIds() != null) {
            adicionarFichasTecnicas(savedRefeicao, dto.fichasTecnicasIds());
        }

        return toResponseDTO(savedRefeicao);
    }

    @Transactional
    public RefeicaoResponseDTO update(Long id, RefeicaoDTO dto) {
        Refeicao refeicao = refeicaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refeição não encontrada"));

        refeicao.setNome(dto.nome());
        refeicao.setKcalTotal(dto.kcalTotal());
        refeicao.setStatus(dto.status());

        List<FichasPorRefeicao> atuais = refeicao.getFichasPorRefeicao();
        Set<Long> novosIds = new HashSet<>(dto.fichasTecnicasIds());

        atuais.removeIf(fp -> !novosIds.remove(fp.getFichaTecnica().getId()));

        if (!novosIds.isEmpty()) {
            List<FichaTecnica> fichasParaAdicionar =
                    fichaTecnicaRepository.findAllById(novosIds);

            for (FichaTecnica ft : fichasParaAdicionar) {
                FichasPorRefeicao assoc = new FichasPorRefeicao();
                assoc.setRefeicao(refeicao);
                assoc.setFichaTecnica(ft);
                assoc.setId(new FichasPorRefeicaoId(refeicao.getId(), ft.getId()));
                atuais.add(assoc);
            }
        }

        Refeicao salva = refeicaoRepository.save(refeicao);
        return toResponseDTO(salva);
    }

    @Transactional(readOnly = true)
    public RefeicaoResponseDTO buscarPorId(Long id) {
        Refeicao refeicao = refeicaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refeição não encontrada"));
        return toResponseDTO(refeicao);
    }

    @Transactional
    public void adicionarFichasTecnicas(Refeicao refeicao, List<Long> fichaIds) {
        List<FichaTecnica> fichas = fichaTecnicaRepository.findAllById(fichaIds);
        List<FichasPorRefeicao> porRefeicao = fichas.stream()
                .map(ft -> new FichasPorRefeicao(refeicao, ft))
                .toList();

        refeicao.getFichasPorRefeicao().addAll(porRefeicao);
    }

    public void atualizaStatus(Long id) {
        Refeicao refeicao = refeicaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Refeição não encontrada"));

        refeicao.setStatus(
                refeicao.getStatus() == Status.ATIVA ?
                        Status.INATIVA :
                        Status.ATIVA
        );

        refeicaoRepository.save(refeicao);
    }

    public List<RefeicaoResponseDTO> buscarPorStatusENutricionista(Status status, Long nutricionistaId) {
        return refeicaoRepository.findByStatusAndNutricionistaId(status, nutricionistaId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<RefeicaoResponseDTO> buscarPorNomeENutricionista(String nome, Long nutricionistaId) {
        return refeicaoRepository.findByNomeAndStatusAndNutricionistaId(nome, Status.ATIVA, nutricionistaId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<RefeicaoResponseDTO> buscarPorStatusEEstabelecimento(Status status, Long estabelecimentoId) {
        return refeicaoRepository.findByStatusAndNutricionistaEstabelecimentoId(status, estabelecimentoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<RefeicaoResponseDTO> buscarPorNomeEEstabelecimento(String nome, Long estabelecimentoId) {
        return refeicaoRepository.findByNomeAndStatusAndNutricionistaEstabelecimentoId(nome, Status.ATIVA, estabelecimentoId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    private RefeicaoResponseDTO toResponseDTO(Refeicao refeicao) {
        List<FichaTecnicaRefeicaoDTO> fichasDTO = refeicao.getFichasPorRefeicao().stream()
                .map(fp -> new FichaTecnicaRefeicaoDTO(
                        fp.getFichaTecnica().getId(),
                        fp.getFichaTecnica().getPreparacao().getNome(),
                        fp.getFichaTecnica().getPerfilNutricional().getVtc()
                ))
                .collect(Collectors.toList());

        return new RefeicaoResponseDTO(
                refeicao.getId(),
                refeicao.getNome(),
                refeicao.getKcalTotal(),
                refeicao.getStatus(),
                fichasDTO);
    }
}