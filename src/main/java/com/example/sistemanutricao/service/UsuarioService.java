package com.example.sistemanutricao.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.sistemanutricao.model.Cargo;
import com.example.sistemanutricao.model.Estabelecimento;
import com.example.sistemanutricao.model.Usuario;
import com.example.sistemanutricao.record.UsuarioDTO.CreateUsuarioDTO;
import com.example.sistemanutricao.record.UsuarioDTO.GetUsuarioDTO;
import com.example.sistemanutricao.record.UsuarioDTO.UpdateUsuarioDTO;
import com.example.sistemanutricao.repository.EstabelecimentoRepository;
import com.example.sistemanutricao.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final EstabelecimentoRepository estabelecimentoRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImagemService imagemService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          EstabelecimentoRepository estabelecimentoRepository,
                          PasswordEncoder passwordEncoder, ImagemService imagemService) {
        this.usuarioRepository = usuarioRepository;
        this.estabelecimentoRepository = estabelecimentoRepository;
        this.passwordEncoder = passwordEncoder;
        this.imagemService = imagemService;
    }


    @Transactional
    public void atualizarImagemPerfil(Long usuarioId, MultipartFile arquivoImagem) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String nomeArquivo = imagemService.armazenarImagem(arquivoImagem);
        usuario.setCaminhoImagem(nomeArquivo);

        usuarioRepository.save(usuario);
    }

    public Resource obterImagemPerfil(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getCaminhoImagem() == null || usuario.getCaminhoImagem().isEmpty()) {
            throw new RuntimeException("Usuário não possui imagem de perfil");
        }

        return imagemService.carregarImagem(usuario.getCaminhoImagem());
    }

    public Usuario create(CreateUsuarioDTO registroDto) {
        Usuario usuario = new Usuario();
        usuario.setUsername(registroDto.username());
        usuario.setEmail(registroDto.email());
        usuario.setSenha(passwordEncoder.encode(registroDto.senha()));
        usuario.setAtivo(false);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void atualizarEstabelecimento(Long usuarioId, Long estabelecimentoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));

        usuario.setEstabelecimento(estabelecimento);
        usuarioRepository.save(usuario);
    }

    public List<GetUsuarioDTO> listAll() {
        return usuarioRepository.findAll().stream()
                .map(this::toGetDTO)
                .collect(Collectors.toList());
    }

    public GetUsuarioDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return toGetDTO(usuario);
    }

    @Transactional
    public GetUsuarioDTO update(Long id, UpdateUsuarioDTO dto) {
        logger.info("Iniciando atualização para usuário ID: {} com DTO: {}", id, dto);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        logger.info("Usuário encontrado: {}", usuario);

        usuario.setUsername(dto.username());
        usuario.setEmail(dto.email());

        // Atualizar senha se fornecida
        if (dto.novaSenha() != null && !dto.novaSenha().isEmpty()) {
            logger.info("Atualizando senha para: {}", dto.novaSenha());
            usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        }

        if (dto.caminhoImagem() != null && !dto.caminhoImagem().isEmpty()) {
            logger.info("Atualizando caminho da imagem para: {}", dto.caminhoImagem());
            usuario.setCaminhoImagem(dto.caminhoImagem());
        }

        logger.info("Usuário antes de salvar: {}", usuario);
        Usuario updated = usuarioRepository.save(usuario);
        logger.info("Usuário salvo com sucesso: {}", updated);
        return toGetDTO(updated);
    }

    @Transactional
    public void updateCargo(Long id, Cargo novoCargo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setCargo(novoCargo);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void toggleAtivo(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setAtivo(!usuario.isAtivo());
        usuarioRepository.save(usuario);
    }

    private GetUsuarioDTO toGetDTO(Usuario usuario) {
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
    }
}