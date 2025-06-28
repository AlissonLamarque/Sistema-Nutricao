package com.example.sistemanutricao.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.example.sistemanutricao.model.FichaTecnica;
import com.example.sistemanutricao.model.Ingrediente;
import com.example.sistemanutricao.model.IngredientesPorFicha;
import com.example.sistemanutricao.model.PerfilNutricional;
import com.example.sistemanutricao.record.IngredienteDTO.IngredienteGetDTO;
import com.example.sistemanutricao.record.IngredientesPorFichaDTO.IngredientePorFichaDTO;
import com.example.sistemanutricao.record.IngredientesPorFichaDTO.IngredientePorFichaGetDTO;
import com.example.sistemanutricao.repository.IngredienteRepository;
import com.example.sistemanutricao.repository.IngredientesPorFichaRepository;

@Service
public class IngredientesPorFichaService {

    private final IngredientesPorFichaRepository ipfRepository;
    private final IngredienteRepository ingredienteRepository;

    public IngredientesPorFichaService(IngredientesPorFichaRepository ipfRepository, IngredienteRepository ingredienteRepository) {
        this.ipfRepository = ipfRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    public List<IngredientePorFichaGetDTO> create(
            List<IngredientePorFichaDTO> dtos,
            FichaTecnica fichaSalva,
            Long perfilNutricionalId
    ) {
        return dtos.stream()
                .map(dto -> {
                    Ingrediente ingrediente = ingredienteRepository.findById(dto.ingredienteId())
                            .orElseThrow(() -> new NoSuchElementException("Ingrediente não encontrado: " + dto.ingredienteId()));

                    IngredientesPorFicha ipf = new IngredientesPorFicha();
                    ipf.setIngrediente(ingrediente);
                    ipf.setFichaTecnica(fichaSalva);
                    ipf.setPerfilNutricional(
                            new PerfilNutricional(perfilNutricionalId)
                    );
                    ipf.setCustoKG(dto.custoKg());
                    ipf.setCustoUsado(dto.custoUsado());
                    ipf.setFc(dto.fc());
                    ipf.setMedidaCaseria(dto.medidaCaseira());
                    ipf.setPb(dto.pb());
                    ipf.setPl(dto.pl());

                    IngredientesPorFicha salvo = ipfRepository.save(ipf);
                    return convertToDto(salvo);
                })
                .toList();
    }

    public List<IngredientePorFichaGetDTO> getAllIngredientesPorFicha() {
        return ipfRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<IngredientePorFichaGetDTO> listarIngredientesPorFichaId(Long fichaId) {
        return ipfRepository.findByFichaTecnicaId(fichaId).stream()
                .map(this::convertToDto)
                .toList();
    }

    public IngredientePorFichaGetDTO update(IngredientePorFichaDTO dto, Long id) {
        IngredientesPorFicha ipf = ipfRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ingrediente não encontrada"));

            ipf.setCustoKG(dto.custoKg());
            ipf.setCustoUsado(dto.custoUsado());
            ipf.setFc(dto.fc());
            ipf.setMedidaCaseria(dto.medidaCaseira());
            ipf.setPb(dto.pb());
            ipf.setPl(dto.pl());
        IngredientesPorFicha ipfSalvo = ipfRepository.save(ipf);

        return convertToDto(ipfSalvo);
    }

    public void delete(Long id) {
        ipfRepository.deleteById(id);
    }

    private IngredientePorFichaGetDTO convertToDto(IngredientesPorFicha ipf) {
        Ingrediente ing = ipf.getIngrediente();
        IngredienteGetDTO ingDto = new IngredienteGetDTO(
                ing.getId(),
                ing.getNome(),
                ing.getPtn(),
                ing.getCho(),
                ing.getLip(),
                ing.getStatus(),
                ing.getSodio(),
                ing.getGorduraSaturada(),
                ing.getUsuario().getId()
        );

        return new IngredientePorFichaGetDTO(
                ipf.getId(),
                ingDto,
                ipf.getCustoKG(),
                ipf.getCustoUsado(),
                ipf.getFc(),
                ipf.getMedidaCaseria(),
                ipf.getPb(),
                ipf.getPl()
        );
    }
}

