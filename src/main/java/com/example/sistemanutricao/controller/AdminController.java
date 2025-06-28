package com.example.sistemanutricao.controller;

import com.example.sistemanutricao.model.Cargo;
import com.example.sistemanutricao.record.EstabelecimentoDTO.CreateEstabelecimentoDTO;
import com.example.sistemanutricao.record.EstabelecimentoDTO.GetEstabelecimentoDTO;
import com.example.sistemanutricao.record.EstabelecimentoDTO.UpdateEstabelecimentoDTO;
import com.example.sistemanutricao.record.UsuarioDTO.GetUsuarioDTO;
import com.example.sistemanutricao.service.EstabelecimentoService;
import com.example.sistemanutricao.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    private final UsuarioService usuarioService;
    private final EstabelecimentoService estabelecimentoService;

    public AdminController(UsuarioService usuarioService,
                             EstabelecimentoService estabelecimentoService) {
        this.usuarioService = usuarioService;
        this.estabelecimentoService = estabelecimentoService;
    }

    @GetMapping
    public String getAdmin() {
        return "Admin/Home";
    }

    @PostMapping("/usuarios/{id}/toggle-ativo")
    public String toggleAtivo(@PathVariable Long id) {
        usuarioService.toggleAtivo(id);
        return "redirect:/admin/usuarios";
    }


    @GetMapping("/usuarios/{id}/estabelecimento")
    public String formVinculoEstabelecimento(@PathVariable Long id, Model model) {
        GetUsuarioDTO usuario = usuarioService.findById(id);
        List<GetEstabelecimentoDTO> estabelecimentos = estabelecimentoService.listAll();

        model.addAttribute("usuario", usuario);
        model.addAttribute("estabelecimentos", estabelecimentos);
        return "Admin/Usuarios/Form";
    }

    @PostMapping("/usuarios/{id}/estabelecimento")
    public String atualizarVinculoEstabelecimento(
            @PathVariable Long id,
            @RequestParam Long estabelecimentoId) {

        usuarioService.atualizarEstabelecimento(id, estabelecimentoId);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios")
    public String listUsuarios(Model model) {
        List<GetUsuarioDTO> usuarios = usuarioService.listAll();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("cargos", Cargo.values());
        return "Admin/Usuarios/List";
    }


    @PostMapping("/usuarios/{id}/cargo")
    public String updateCargo(@PathVariable Long id, @RequestParam Cargo cargo) {
        usuarioService.updateCargo(id, cargo);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/estabelecimentos")
    public String listEstabelecimentos(Model model) {
        List<GetEstabelecimentoDTO> estabelecimentos = estabelecimentoService.listAll();
        model.addAttribute("estabelecimentos", estabelecimentos);
        return "Admin/Estabelecimentos/List";
    }

    @GetMapping("/estabelecimentos/novo")
    public String createForm(Model model) {
        model.addAttribute("estabelecimento", new CreateEstabelecimentoDTO(null,""));
        return "Admin/Estabelecimentos/Form";
    }

    @PostMapping("/estabelecimentos")
    public String create(@ModelAttribute CreateEstabelecimentoDTO dto) {
        estabelecimentoService.create(dto);
        return "redirect:/admin/estabelecimentos";
    }

    @GetMapping("/estabelecimentos/{id}/editar")
    public String updateForm(@PathVariable Long id, Model model) {
        GetEstabelecimentoDTO estabelecimento = estabelecimentoService.findById(id);
        model.addAttribute("estabelecimento", estabelecimento);
        return "Admin/Estabelecimentos/Form";
    }

    @PostMapping("/estabelecimentos/{id}")
    public String update(@PathVariable Long id, @ModelAttribute UpdateEstabelecimentoDTO dto) {
        estabelecimentoService.update(id, dto);
        return "redirect:/admin/estabelecimentos";
    }
}
