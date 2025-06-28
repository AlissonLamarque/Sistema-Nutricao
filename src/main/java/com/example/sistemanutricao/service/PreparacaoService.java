package com.example.sistemanutricao.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.example.sistemanutricao.model.Preparacao;
import com.example.sistemanutricao.record.PreparacaoDTO.PreparacaoCreateDTO;
import com.example.sistemanutricao.record.PreparacaoDTO.PreparacaoGetDTO;
import com.example.sistemanutricao.record.PreparacaoDTO.PreparacaoUpdateDTO;
import com.example.sistemanutricao.repository.PreparacaoRepository;

@Service
public class PreparacaoService {

    private final PreparacaoRepository preparacaoRepository;

    public PreparacaoService(PreparacaoRepository preparacaoRepository) {
        this.preparacaoRepository = preparacaoRepository;
    }

    public PreparacaoGetDTO create(PreparacaoCreateDTO dto) {

        Preparacao prep = new Preparacao();
            prep.setNome(dto.nome());
            prep.setNumero(dto.numero());
            prep.setTempoPreparo(dto.tempoPreparo());
            prep.setEquipamentos(dto.equipamentos());
            prep.setModoPreparo(dto.modoPreparo());
            prep.setPorcentAgua(dto.porcentAgua());
            prep.setQntdAgua(dto.qntdAgua());
            prep.setFcc(dto.fcc());
            prep.setRendimento(dto.rendimento());
            prep.setCategoria(dto.categoria());
        Preparacao preparacaoSalvo = preparacaoRepository.save(prep);

        return convertToDto(preparacaoSalvo);
    }

    public PreparacaoGetDTO getPreparacaoById(Long id) {
        Preparacao prep = preparacaoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Preparacao não encontrada"));
        return convertToDto(prep);
    }

    public Preparacao update(PreparacaoUpdateDTO dto) {
        Preparacao prep = preparacaoRepository.findById(dto.id())
                .orElseThrow(() -> new NoSuchElementException("Preparacao não encontrada"));
            prep.setNome(dto.nome());
            prep.setNumero(dto.numero());
            prep.setTempoPreparo(dto.tempoPreparo());
            prep.setEquipamentos(dto.equipamentos());
            prep.setModoPreparo(dto.modoPreparo());
            prep.setPorcentAgua(dto.porcentAgua());
            prep.setQntdAgua(dto.qntdAgua());
            prep.setFcc(dto.fcc());
            prep.setRendimento(dto.rendimento());
            prep.setCategoria(dto.categoria());

        return preparacaoRepository.save(prep);
    }

    public PreparacaoGetDTO convertToDto(Preparacao prep) {
        return new PreparacaoGetDTO(
                prep.getId(), prep.getNome(), prep.getNumero(),
                prep.getTempoPreparo(), prep.getEquipamentos(),
                prep.getModoPreparo(), prep.getPorcentAgua(),
                prep.getQntdAgua(), prep.getFcc(), prep.getRendimento(),
                prep.getCategoria());
    }
}

