package com.example.sistemanutricao.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.sistemanutricao.record.UsuarioDTO.CreateUsuarioDTO;
import com.example.sistemanutricao.record.UsuarioDTO.GetUsuarioDTO;
import com.example.sistemanutricao.record.UsuarioDTO.UpdateUsuarioDTO;
import com.example.sistemanutricao.security.UsuarioSecurity;
import com.example.sistemanutricao.service.ImagemService;
import com.example.sistemanutricao.service.UsuarioService;

@Controller
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final ImagemService imagemService;

    public UsuarioController(UsuarioService usuarioService
    , PasswordEncoder passwordEncoder, ImagemService imagemService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.imagemService = imagemService;
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(@AuthenticationPrincipal UsuarioSecurity usuarioPrincipal,
                                Model model) {
        GetUsuarioDTO usuarioDTO = usuarioService.findById(usuarioPrincipal.getId());
        model.addAttribute("usuario", usuarioDTO);
        return "Perfil";
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<Resource> obterImagemUsuario(@PathVariable Long id) {
        try {
            Resource resource = usuarioService.obterImagemPerfil(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (Exception e) {
            logger.error("Erro ao obter imagem do usuário {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/editar")
    public String editarPerfil(@ModelAttribute("usuario") UpdateUsuarioDTO usuarioDTO,
                               BindingResult result,
                               @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal,
                               @RequestParam(value = "imagemPerfil", required = false) MultipartFile arquivoImagem,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "Perfil";
        }

        // Verificar se as novas senhas coincidem (se foram fornecidas)
        if (usuarioDTO.novaSenha() != null && !usuarioDTO.novaSenha().isEmpty()) {
            // Verificar se a senha atual está correta
            if (usuarioDTO.senhaAtual() == null || !passwordEncoder.matches(usuarioDTO.senhaAtual(), usuarioPrincipal.getPassword())) {
                result.rejectValue("senhaAtual", "error.usuario", "Senha atual incorreta");
                return "Perfil";
            }
            
            if (!usuarioDTO.novaSenha().equals(usuarioDTO.confirmarNovaSenha())) {
                result.rejectValue("confirmarNovaSenha", "error.usuario", "As novas senhas não coincidem");
                return "Perfil";
            }
        }

        try {
            // Usar o novo método que processa imagem e dados do perfil
            usuarioService.atualizarPerfilComImagem(usuarioPrincipal.getId(), usuarioDTO, arquivoImagem);
            redirectAttributes.addFlashAttribute("success", "Perfil atualizado com sucesso!");
            return "redirect:/perfil";
        } catch (Exception e) {
            logger.error("Erro ao atualizar perfil: {}", e.getMessage());
            result.rejectValue("", "error.usuario", "Erro ao atualizar perfil: " + e.getMessage());
            return "Perfil";
        }
    }

    @GetMapping
    public String loginPage() {
        return "login";
    }

    @PostMapping("/registrar")
    public String registrarUsuario(@ModelAttribute("usuario") CreateUsuarioDTO registroDto,
                                   BindingResult result) {
        if (result.hasErrors()) {
            return "login";
        }

        usuarioService.create(registroDto);
        return "redirect:/registro-sucesso";
    }

    @GetMapping("/registro-sucesso")
    public String mostrarSucessoRegistro() {
        return "Registrado";
    }

    @GetMapping("/auth/status")
    public ResponseEntity<Map<String, Object>> verificarStatusAutenticacao(
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Map<String, Object> response = new HashMap<>();
        
        if (usuarioPrincipal != null) {
            response.put("autenticado", true);
            response.put("cargo", usuarioPrincipal.getCargo().name());
            response.put("username", usuarioPrincipal.getUsername());
            return ResponseEntity.ok(response);
        } else {
            response.put("autenticado", false);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/teste-imagem/{id}")
    public ResponseEntity<String> testeImagem(@PathVariable Long id) {
        try {
            GetUsuarioDTO usuario = usuarioService.findById(id);
            return ResponseEntity.ok("Usuário: " + usuario.username() + 
                                   ", Caminho da imagem: " + usuario.caminhoImagem() +
                                   ", URL completa: /" + usuario.caminhoImagem());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

}

