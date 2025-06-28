package com.example.sistemanutricao.service;

import com.example.sistemanutricao.model.PerfilNutricional;
import com.example.sistemanutricao.record.PerfilNutricionalDTO.PerfilNutricionalCreateDTO;
import com.example.sistemanutricao.record.PerfilNutricionalDTO.PerfilNutricionalGetDTO;
import com.example.sistemanutricao.record.PerfilNutricionalDTO.PerfilNutricionalUpdateDTO;
import com.example.sistemanutricao.repository.PerfilNutricionalRepository;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Service
public class PerfilNutricionalService {


    private final PerfilNutricionalRepository perfilRepository;

    public PerfilNutricionalService(PerfilNutricionalRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public PerfilNutricionalGetDTO create(PerfilNutricionalCreateDTO dto) {

        PerfilNutricional perfil = new PerfilNutricional();
            perfil.setVtc(dto.vtc());
            perfil.setKcalPTN(dto.kcalPtn());
            perfil.setKcalCHO(dto.kcalCho());
            perfil.setKcalLIP(dto.kcalLip());
            perfil.setGramasPTN(dto.gramasPtn());
            perfil.setGramasCHO(dto.gramasCho());
            perfil.setGramasLIP(dto.gramasLip());
            perfil.setGramasSodio(dto.gramasSodio());
            perfil.setGramasSaturada(dto.gramasSaturada());
            perfil.setPorcentPTN(dto.porcentPtn());
            perfil.setPorcentCHO(dto.porcentCho());
            perfil.setPorcentLIP(dto.porcentLip());

        PerfilNutricional perfilSalvo = perfilRepository.save(perfil);

        return convertToDto(perfilSalvo);
    }

    public PerfilNutricionalGetDTO getPerfilNutricionalById(Long id) {
        PerfilNutricional perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("PerfilNutricional não encontrada"));
        return convertToDto(perfil);
    }

    public PerfilNutricional update(Long id, PerfilNutricionalUpdateDTO dto) {
        PerfilNutricional perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Perfil Nutricional não encontrada"));
            perfil.setVtc(dto.vtc());
            perfil.setKcalPTN(dto.kcalPtn());
            perfil.setKcalCHO(dto.kcalCho());
            perfil.setKcalLIP(dto.kcalLip());
            perfil.setGramasPTN(dto.gramasPtn());
            perfil.setGramasCHO(dto.gramasCho());
            perfil.setGramasLIP(dto.gramasLip());
            perfil.setGramasSodio(dto.gramasSodio());
            perfil.setGramasSaturada(dto.gramasSaturada());
            perfil.setPorcentPTN(dto.porcentPtn());
            perfil.setPorcentCHO(dto.porcentCho());
            perfil.setPorcentLIP(dto.porcentLip());

        return perfilRepository.save(perfil);
    }
    private PerfilNutricionalGetDTO convertToDto(PerfilNutricional perfil) {
        return new PerfilNutricionalGetDTO(
                perfil.getId(), perfil.getVtc(), perfil.getKcalPTN(),
                perfil.getKcalCHO(), perfil.getKcalLIP(), perfil.getGramasPTN(),
                perfil.getGramasCHO(), perfil.getGramasLIP(), perfil.getGramasSodio(),
                perfil.getGramasSaturada(), perfil.getPorcentPTN(), perfil.getPorcentCHO(),
                perfil.getPorcentLIP());
    }

}
