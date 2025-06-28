package com.example.sistemanutricao.service;

import java.math.BigDecimal;
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

                    // Calcular valores nutricionais baseados no PL
                    BigDecimal pl = dto.pl();
                    if (pl != null && pl.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal ptn = ingrediente.getPtn() != null ? ingrediente.getPtn() : BigDecimal.ZERO;
                        BigDecimal cho = ingrediente.getCho() != null ? ingrediente.getCho() : BigDecimal.ZERO;
                        BigDecimal lip = ingrediente.getLip() != null ? ingrediente.getLip() : BigDecimal.ZERO;
                        BigDecimal sodio = ingrediente.getSodio() != null ? ingrediente.getSodio() : BigDecimal.ZERO;
                        BigDecimal gorduraSaturada = ingrediente.getGorduraSaturada() != null ? ingrediente.getGorduraSaturada() : BigDecimal.ZERO;
                        
                        ipf.setPtnCalculado(ptn.multiply(pl).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                        ipf.setChoCalculado(cho.multiply(pl).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                        ipf.setLipCalculado(lip.multiply(pl).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                        ipf.setSodioCalculado(sodio.multiply(pl).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                        ipf.setGorduraSaturadaCalculada(gorduraSaturada.multiply(pl).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        ipf.setPtnCalculado(BigDecimal.ZERO);
                        ipf.setChoCalculado(BigDecimal.ZERO);
                        ipf.setLipCalculado(BigDecimal.ZERO);
                        ipf.setSodioCalculado(BigDecimal.ZERO);
                        ipf.setGorduraSaturadaCalculada(BigDecimal.ZERO);
                    }

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
        List<IngredientesPorFicha> lista = ipfRepository.buscarPorFichaTecnicaId(fichaId);
        return lista.stream()
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

        // Recalcular valores nutricionais baseados no PL
        BigDecimal pl = dto.pl();
        Ingrediente ingrediente = ipf.getIngrediente();
        if (pl != null && pl.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ptn = ingrediente.getPtn() != null ? ingrediente.getPtn() : BigDecimal.ZERO;
            BigDecimal cho = ingrediente.getCho() != null ? ingrediente.getCho() : BigDecimal.ZERO;
            BigDecimal lip = ingrediente.getLip() != null ? ingrediente.getLip() : BigDecimal.ZERO;
            BigDecimal sodio = ingrediente.getSodio() != null ? ingrediente.getSodio() : BigDecimal.ZERO;
            BigDecimal gorduraSaturada = ingrediente.getGorduraSaturada() != null ? ingrediente.getGorduraSaturada() : BigDecimal.ZERO;
            
            ipf.setPtnCalculado(ptn.multiply(pl).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
            ipf.setChoCalculado(cho.multiply(pl).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
            ipf.setLipCalculado(lip.multiply(pl).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
            ipf.setSodioCalculado(sodio.multiply(pl).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
            ipf.setGorduraSaturadaCalculada(gorduraSaturada.multiply(pl).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
        } else {
            ipf.setPtnCalculado(BigDecimal.ZERO);
            ipf.setChoCalculado(BigDecimal.ZERO);
            ipf.setLipCalculado(BigDecimal.ZERO);
            ipf.setSodioCalculado(BigDecimal.ZERO);
            ipf.setGorduraSaturadaCalculada(BigDecimal.ZERO);
        }

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
                ing.getPtn() != null ? ing.getPtn() : BigDecimal.ZERO,
                ing.getCho() != null ? ing.getCho() : BigDecimal.ZERO,
                ing.getLip() != null ? ing.getLip() : BigDecimal.ZERO,
                ing.getStatus(),
                ing.getSodio() != null ? ing.getSodio() : BigDecimal.ZERO,
                ing.getGorduraSaturada() != null ? ing.getGorduraSaturada() : BigDecimal.ZERO,
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
                ipf.getPl(),
                ipf.getPtnCalculado() != null ? ipf.getPtnCalculado() : BigDecimal.ZERO,
                ipf.getChoCalculado() != null ? ipf.getChoCalculado() : BigDecimal.ZERO,
                ipf.getLipCalculado() != null ? ipf.getLipCalculado() : BigDecimal.ZERO,
                ipf.getSodioCalculado() != null ? ipf.getSodioCalculado() : BigDecimal.ZERO,
                ipf.getGorduraSaturadaCalculada() != null ? ipf.getGorduraSaturadaCalculada() : BigDecimal.ZERO
        );
    }
}

