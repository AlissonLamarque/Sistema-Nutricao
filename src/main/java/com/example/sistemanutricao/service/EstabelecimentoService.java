package com.example.sistemanutricao.service;

import com.example.sistemanutricao.record.EstabelecimentoDTO.CreateEstabelecimentoDTO;
import com.example.sistemanutricao.record.EstabelecimentoDTO.GetEstabelecimentoDTO;
import com.example.sistemanutricao.record.EstabelecimentoDTO.UpdateEstabelecimentoDTO;
import com.example.sistemanutricao.model.Estabelecimento;
import com.example.sistemanutricao.record.UsuarioDTO.GetUsuarioDTO;
import com.example.sistemanutricao.repository.EstabelecimentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstabelecimentoService {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public EstabelecimentoService(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    @Transactional
    public GetEstabelecimentoDTO create(CreateEstabelecimentoDTO dto) {
        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setNome(dto.nome());

        Estabelecimento saved = estabelecimentoRepository.save(estabelecimento);
        return toGetDTO(saved);
    }

    public List<GetEstabelecimentoDTO> listAll() {
        return estabelecimentoRepository.findAll().stream()
                .map(this::toGetDTO)
                .collect(Collectors.toList());
    }

    public GetEstabelecimentoDTO findById(Long id) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));
        return toGetDTO(estabelecimento);
    }

    @Transactional
    public GetEstabelecimentoDTO update(Long id, UpdateEstabelecimentoDTO dto) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));

        estabelecimento.setNome(dto.nome());

        Estabelecimento updated = estabelecimentoRepository.save(estabelecimento);
        return toGetDTO(updated);
    }

    private GetEstabelecimentoDTO toGetDTO(Estabelecimento estabelecimento) {
        List<GetUsuarioDTO> usuariosDTO = List.of();

        if (estabelecimento.getUsuario() != null && !estabelecimento.getUsuario().isEmpty()) {
            usuariosDTO = estabelecimento.getUsuario().stream()
                    .map(usuario -> {
                        Long estabelecimentoId = null;
                        String estabelecimentoNome = null;

                        if (usuario.getEstabelecimento() != null) {
                            estabelecimentoId = usuario.getEstabelecimento().getId();
                            estabelecimentoNome = usuario.getEstabelecimento().getNome();
                        }

                        return new GetUsuarioDTO(
                                usuario.getId(),
                                usuario.getUsername(),
                                usuario.getEmail(),
                                usuario.getCargo(),
                                estabelecimentoId,
                                estabelecimentoNome,
                                usuario.isAtivo(),
                                usuario.getCaminhoImagem()
                        );
                    })
                    .collect(Collectors.toList());
        }

        return new GetEstabelecimentoDTO(
                estabelecimento.getId(),
                estabelecimento.getNome(),
                usuariosDTO
        );
    }
}