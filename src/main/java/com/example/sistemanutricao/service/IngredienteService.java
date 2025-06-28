package com.example.sistemanutricao.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.example.sistemanutricao.model.Ingrediente;
import com.example.sistemanutricao.model.Status;
import com.example.sistemanutricao.model.Usuario;
import com.example.sistemanutricao.record.IngredienteDTO.IngredienteComTagDTO;
import com.example.sistemanutricao.record.IngredienteDTO.IngredienteCreateDTO;
import com.example.sistemanutricao.record.IngredienteDTO.IngredienteGetDTO;
import com.example.sistemanutricao.record.IngredienteDTO.IngredienteUpdateDTO;
import com.example.sistemanutricao.repository.IngredienteRepository;
import com.example.sistemanutricao.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final UsuarioRepository usuarioRepository;

    public IngredienteService(IngredienteRepository ingredienteRepository, UsuarioRepository usuarioRepository) {
        this.ingredienteRepository = ingredienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public IngredienteGetDTO create(IngredienteCreateDTO dto, Long usuarioId) {
        Ingrediente ing = new Ingrediente();
        ing.setNome(dto.nome());
        ing.setPtn(dto.ptn());
        ing.setCho(dto.cho());
        ing.setLip(dto.lip());
        ing.setStatus(dto.status());
        ing.setSodio(dto.sodio());
        ing.setGorduraSaturada(dto.gorduraSaturada());

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        ing.setUsuario(usuario);

        Ingrediente ingredienteSalvo = ingredienteRepository.save(ing);
        return convertToDto(ingredienteSalvo);
    }

    public IngredienteGetDTO update(IngredienteUpdateDTO dto, IngredienteGetDTO dtoId) {
        Ingrediente ing = ingredienteRepository.findById(dtoId.id())
                .orElseThrow(() -> new NoSuchElementException("Ingrediente não encontrada"));
            ing.setNome(dto.nome());
            ing.setPtn(dto.ptn());
            ing.setCho(dto.cho());
            ing.setLip(dto.lip());
            ing.setStatus(dto.status());
            ing.setSodio(dto.sodio());
            ing.setGorduraSaturada(dto.gorduraSaturada());
        Ingrediente ingredienteSalvo = ingredienteRepository.save(ing);

        return convertToDto(ingredienteSalvo);
    }

    public IngredienteGetDTO getIngredienteById(Long id) {
        Ingrediente ing = ingredienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ingrediente não encontrada"));
        return convertToDto(ing);
    }

    public List<IngredienteGetDTO> buscarPorStatusEUsuario(Status status, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return new ArrayList<>();
        }

        List<Ingrediente> ingredientes = ingredienteRepository
                .findByStatusAndUsuario(status, usuario);
        return ingredientes.stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<IngredienteGetDTO> buscarPorStatusEUsuarios(Status status, Long... usuariosIds) {
        List<Long> ids = Arrays.asList(usuariosIds);
        List<Ingrediente> ingredientes = ingredienteRepository.findByStatusAndUsuarioIdIn(status, ids);

        return ingredientes.stream()
                .map(this::convertToDto)
                .toList();
    }



    public List<Ingrediente> buscarIngredientesDoUsuarioTaco() {
        return usuarioRepository.findById(4L)
                .map(ingredienteRepository::findByUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário taco não encontrado"));
    }

    public void atualizaStatus(Long id) {
        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ingrediente não encontrado"));

        ingrediente.setStatus(
                ingrediente.getStatus() == Status.ATIVA ?
                        Status.INATIVA :
                        Status.ATIVA
        );

        ingredienteRepository.save(ingrediente);
    }

    public List<IngredienteGetDTO> buscarPorNome(String nome, Long usuarioId) {

        return ingredienteRepository.findByNomeAndStatusAndUsuario_Id(nome, Status.ATIVA, usuarioId).stream()
                .map(this::convertToDto).toList();
    }

    public List<IngredienteGetDTO> buscarPorPTN(BigDecimal ptn, Long usuarioId) {

        return ingredienteRepository.findByPtnAndStatusAndUsuario_Id(ptn, Status.ATIVA, usuarioId).stream()
                .map(this::convertToDto).toList();
    }

    public List<IngredienteGetDTO> buscarPorCHO(BigDecimal cho, Long usuarioId) {

        return ingredienteRepository.findByChoAndStatusAndUsuario_Id(cho, Status.ATIVA, usuarioId).stream()
                .map(this::convertToDto).toList();
    }

    public List<IngredienteGetDTO> buscarPorLIP(BigDecimal lip, Long usuarioId) {

        return ingredienteRepository.findByLipAndStatusAndUsuario_Id(lip, Status.ATIVA, usuarioId).stream()
                .map(this::convertToDto).toList();
    }

    public List<IngredienteGetDTO> buscarPorSodio(BigDecimal sodio, Long usuarioId) {

        return ingredienteRepository.findBySodioAndStatusAndUsuario_Id(sodio, Status.ATIVA, usuarioId).stream()
                .map(this::convertToDto).toList();
    }

    public List<IngredienteGetDTO> buscarPorGorduraSaturada(BigDecimal gorduraSaturada, Long usuarioId) {
        return ingredienteRepository.findByGorduraSaturadaAndStatusAndUsuario_Id(gorduraSaturada, Status.ATIVA, usuarioId).stream()
                .map(this::convertToDto).toList();
    }

    private static final Long USUARIO_ID_4 = 4L;

    public List<IngredienteGetDTO> buscarPorNomeUsuario4(String nome) {
        return ingredienteRepository.findByNomeAndStatusAndUsuario_Id(nome, Status.ATIVA, USUARIO_ID_4)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<IngredienteGetDTO> buscarPorPTNUsuario4(BigDecimal ptn) {
        return ingredienteRepository.findByPtnAndStatusAndUsuario_Id(ptn, Status.ATIVA, USUARIO_ID_4)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<IngredienteGetDTO> buscarPorCHOUsuario4(BigDecimal cho) {
        return ingredienteRepository.findByChoAndStatusAndUsuario_Id(cho, Status.ATIVA, USUARIO_ID_4)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<IngredienteGetDTO> buscarPorLIPUsuario4(BigDecimal lip) {
        return ingredienteRepository.findByLipAndStatusAndUsuario_Id(lip, Status.ATIVA, USUARIO_ID_4)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<IngredienteGetDTO> buscarPorSodioUsuario4(BigDecimal sodio) {
        return ingredienteRepository.findBySodioAndStatusAndUsuario_Id(sodio, Status.ATIVA, USUARIO_ID_4)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<IngredienteGetDTO> buscarPorGorduraSaturadaUsuario4(BigDecimal gorduraSaturada) {
        return ingredienteRepository.findByGorduraSaturadaAndStatusAndUsuario_Id(gorduraSaturada, Status.ATIVA, USUARIO_ID_4)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // Métodos para pesquisa por tags
    public List<IngredienteComTagDTO> buscarPorTag(String campo, String tag, Long usuarioId) {
        List<Ingrediente> ingredientes = ingredienteRepository.findByStatusAndUsuario_Id(Status.ATIVA, usuarioId);
        return ingredientes.stream()
                .map(ing -> new IngredienteComTagDTO(
                        ing.getId(),
                        ing.getNome(),
                        ing.getPtn(),
                        ing.getCho(),
                        ing.getLip(),
                        ing.getStatus(),
                        ing.getSodio(),
                        ing.getGorduraSaturada(),
                        ing.getUsuario().getId(),
                        determinarTag(ing, campo)
                ))
                .filter(ing -> ing.tag().equalsIgnoreCase(tag))
                .toList();
    }

    public List<IngredienteComTagDTO> buscarPorTagUsuario4(String campo, String tag) {
        List<Ingrediente> ingredientes = ingredienteRepository.findByStatusAndUsuario_Id(Status.ATIVA, USUARIO_ID_4);
        return ingredientes.stream()
                .map(ing -> new IngredienteComTagDTO(
                        ing.getId(),
                        ing.getNome(),
                        ing.getPtn(),
                        ing.getCho(),
                        ing.getLip(),
                        ing.getStatus(),
                        ing.getSodio(),
                        ing.getGorduraSaturada(),
                        ing.getUsuario().getId(),
                        determinarTag(ing, campo)
                ))
                .filter(ing -> ing.tag().equalsIgnoreCase(tag))
                .toList();
    }

    private String determinarTag(Ingrediente ingrediente, String campo) {
        BigDecimal valor = obterValorCampo(ingrediente, campo);
        if (valor == null) return "Baixa";

        // Definir limites para cada campo
        switch (campo.toLowerCase()) {
            case "ptn":
                return valor.compareTo(new BigDecimal("10")) >= 0 ? "Alta" : 
                       valor.compareTo(new BigDecimal("5")) >= 0 ? "Media" : "Baixa";
            case "cho":
                return valor.compareTo(new BigDecimal("30")) >= 0 ? "Alta" : 
                       valor.compareTo(new BigDecimal("15")) >= 0 ? "Media" : "Baixa";
            case "lip":
                return valor.compareTo(new BigDecimal("10")) >= 0 ? "Alta" : 
                       valor.compareTo(new BigDecimal("5")) >= 0 ? "Media" : "Baixa";
            case "sodio":
                return valor.compareTo(new BigDecimal("500")) >= 0 ? "Alta" : 
                       valor.compareTo(new BigDecimal("200")) >= 0 ? "Media" : "Baixa";
            case "gorduras":
                return valor.compareTo(new BigDecimal("5")) >= 0 ? "Alta" : 
                       valor.compareTo(new BigDecimal("2")) >= 0 ? "Media" : "Baixa";
            default:
                return "Baixa";
        }
    }

    private BigDecimal obterValorCampo(Ingrediente ingrediente, String campo) {
        switch (campo.toLowerCase()) {
            case "ptn":
                return ingrediente.getPtn();
            case "cho":
                return ingrediente.getCho();
            case "lip":
                return ingrediente.getLip();
            case "sodio":
                return ingrediente.getSodio();
            case "gorduras":
                return ingrediente.getGorduraSaturada();
            default:
                return null;
        }
    }

    private IngredienteGetDTO convertToDto(Ingrediente ingre) {
        return new IngredienteGetDTO(
                ingre.getId(), ingre.getNome(), ingre.getPtn(),
                ingre.getCho(), ingre.getLip(), ingre.getStatus(),
                ingre.getSodio(), ingre.getGorduraSaturada(), ingre.getUsuario().getId());
    }
}

