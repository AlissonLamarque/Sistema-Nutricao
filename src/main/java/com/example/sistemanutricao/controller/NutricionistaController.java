package com.example.sistemanutricao.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sistemanutricao.model.Categoria;
import com.example.sistemanutricao.model.Ingrediente;
import com.example.sistemanutricao.model.Status;
import static com.example.sistemanutricao.model.Status.ATIVA;
import com.example.sistemanutricao.model.StatusCriacao;
import static com.example.sistemanutricao.model.StatusCriacao.COMPLETA;
import com.example.sistemanutricao.record.FichaTecnicaDTO.FichaTecnicaCreateDTO;
import com.example.sistemanutricao.record.FichaTecnicaDTO.FichaTecnicaGetDTO;
import com.example.sistemanutricao.record.FichaTecnicaDTO.FichaTecnicaUpdateDTO;
import com.example.sistemanutricao.record.IngredienteDTO.IngredienteCreateDTO;
import com.example.sistemanutricao.record.IngredienteDTO.IngredienteGetDTO;
import com.example.sistemanutricao.record.IngredienteDTO.IngredienteUpdateDTO;
import com.example.sistemanutricao.record.PerfilNutricionalDTO.PerfilNutricionalCreateDTO;
import com.example.sistemanutricao.record.PerfilNutricionalDTO.PerfilNutricionalGetDTO;
import com.example.sistemanutricao.record.PreparacaoDTO.PreparacaoCreateDTO;
import com.example.sistemanutricao.record.RefeicaoDTO.FichaTecnicaRefeicaoDTO;
import com.example.sistemanutricao.record.RefeicaoDTO.RefeicaoDTO;
import com.example.sistemanutricao.record.RefeicaoDTO.RefeicaoResponseDTO;
import com.example.sistemanutricao.repository.UsuarioRepository;
import com.example.sistemanutricao.security.UsuarioSecurity;
import com.example.sistemanutricao.service.FichaTecnicaService;
import com.example.sistemanutricao.service.IngredienteService;
import com.example.sistemanutricao.service.PdfService;
import com.example.sistemanutricao.service.PerfilNutricionalService;
import com.example.sistemanutricao.service.RefeicaoService;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/nutricionista")
@CrossOrigin("*")
public class NutricionistaController {

    private final FichaTecnicaService fichaTecnicaService;
    private final IngredienteService ingredienteService;
    private final PerfilNutricionalService perfilService;
    private final RefeicaoService refeicaoService;
    private final UsuarioRepository usuarioRepository;
    private final PdfService pdfService;

    public NutricionistaController(FichaTecnicaService fichaTecnicaService,
                                  IngredienteService ingredienteService,
                                  PerfilNutricionalService perfilService,
                                   RefeicaoService refeicaoService,
                                   UsuarioRepository usuarioRepository,
                                   PdfService pdfService) {
        this.fichaTecnicaService = fichaTecnicaService;
        this.ingredienteService = ingredienteService;
        this.perfilService = perfilService;
        this.refeicaoService = refeicaoService;
        this.usuarioRepository = usuarioRepository;
        this.pdfService = pdfService;
    }

    @GetMapping
    public String get() {
        return "/Nutri/Home";
    }

    @GetMapping("/fichas")
    public String listarTodasFichas(
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {

        Long usuarioId = usuarioPrincipal.getId();

        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorStatusENutricionistaEStatusCriacao(ATIVA, usuarioId, COMPLETA);
        model.addAttribute("fichas", fichas);
        model.addAttribute("statusAtual", ATIVA);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/custoPerCapita")
    public String buscarPorCustoPerCapita(
            @RequestParam BigDecimal custoPerCapita,
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorCustoPerCapitaENutricionista(custoPerCapita, usuarioId);
        model.addAttribute("fichas", fichas);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/custoTotal")
    public String buscarPorCustoTotal(
            @RequestParam BigDecimal custoTotal,
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorCustoTotalENutricionista(custoTotal, usuarioId);
        model.addAttribute("fichas", fichas);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/por-nome")
    public String buscarPorNomePreparacao(@RequestParam String nome, Model model,
                                          @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorNomePreparacaoENutricionista(nome, usuarioId);
        model.addAttribute("fichas", fichas);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/por-status")
    public String buscarPorStatus(
            @RequestParam Status status,
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorStatusENutricionista(status, usuarioId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("statusAtual", status);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/por-statusCriacao")
    public String buscarPorStatus(
            @RequestParam StatusCriacao statusCriacao,
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorStatusCriacaoENutricionista(statusCriacao, usuarioId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("statusCriacaoAtual", statusCriacao);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/por-rendimento")
    public String buscarPorRendimento(@RequestParam BigDecimal rendimento, Model model,
                                      @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorRendimentoPreparacaoENutricionista(rendimento, usuarioId);
        model.addAttribute("fichas", fichas);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/por-numero")
    public String buscarPorNumero(@RequestParam Integer numero, Model model,
                                  @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorNumeroPreparacaoENutricionista(numero, usuarioId);
        model.addAttribute("fichas", fichas);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/por-vtc")
    public String buscarPorVtc(@RequestParam BigDecimal vtc, Model model,
                               @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorVtcPerfilNutricionalENutricionista(vtc, usuarioId);
        model.addAttribute("fichas", fichas);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/por-gramas-ptn")
    public String buscarPorGramasPTN(@RequestParam BigDecimal gramasPTN, Model model,
                                     @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorGramasPTNPerfilNutricionalENutricionista(gramasPTN, usuarioId);
        model.addAttribute("fichas", fichas);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/por-gramas-cho")
    public String buscarPorGramasCHO(@RequestParam BigDecimal gramasCHO, Model model,
                                     @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorGramasCHOPerfilNutricionalENutricionista(gramasCHO, usuarioId);
        model.addAttribute("fichas", fichas);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/por-gramas-lip")
    public String buscarPorGramasLIP(@RequestParam BigDecimal gramasLIP, Model model,
                                     @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorGramasLIPPerfilNutricionalENutricionista(gramasLIP, usuarioId);
        model.addAttribute("fichas", fichas);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/por-gramas-sodio")
    public String buscarPorGramasSodio(@RequestParam BigDecimal gramasSodio, Model model,
                                       @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorGramasSodioPerfilNutricionalENutricionista(gramasSodio, usuarioId);
        model.addAttribute("fichas", fichas);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/por-gramas-saturada")
    public String buscarPorGramasSaturada(@RequestParam BigDecimal gramasSaturada, Model model,
                                          @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorGramasSaturadaPerfilNutricionalENutricionista(gramasSaturada, usuarioId);
        model.addAttribute("fichas", fichas);
        return "Nutri/Ficha/List";
    }

    @GetMapping("/fichas/por-categoria")
    public String buscarPorCategoriaPreparacao(
            @RequestParam(name = "categoria") String nomeCategoria,
            Model model) {
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorCategoriaPreparacao(nomeCategoria);
        model.addAttribute("fichas", fichas);
        return "Nutri/Ficha/List";
    }


    @GetMapping("/fichas/{id}")
    public String mostrarFichaPorId(@PathVariable Long id, Model model) {
        try {
            FichaTecnicaGetDTO ficha = fichaTecnicaService.getFichaById(id);
            model.addAttribute("ficha", ficha);
            return "Nutri/Ficha/Detail";
        } catch (EntityNotFoundException e) {
            return "redirect:/fichas?error=Ficha não encontrada";
        }
    }

    @PostMapping("/fichas/toggle-status/{id}")
    public String toggleStatus(@PathVariable Long id) {
        fichaTecnicaService.atualizaStatus(id);
        return "redirect:/nutricionista/fichas/" + id;
    }

    @GetMapping("/fichas/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model,
                                          @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<IngredienteGetDTO> ingredientesDisponiveis = ingredienteService
            .buscarPorStatusEUsuarios(Status.ATIVA, usuarioId, 4L);
        PerfilNutricionalGetDTO perfil = perfilService.getPerfilNutricionalById(id);
        FichaTecnicaGetDTO fichas = fichaTecnicaService.getFichaById(id);
        model.addAttribute("ingredientesDisponiveis", ingredientesDisponiveis);
        model.addAttribute("perfil", perfil);
        model.addAttribute("ficha", fichas);
        return "Nutri/Ficha/FormEdit";
    }

    @PostMapping("/fichas/editar/{id}")
    public String updateFichaTecnica(
            @PathVariable Long id,
            @ModelAttribute("ficha") FichaTecnicaUpdateDTO dto
    ) {
        FichaTecnicaGetDTO updatedFicha = fichaTecnicaService.update(id, dto);
        return "redirect:/nutricionista/fichas";
    }


    @GetMapping("/fichas/nova")
    public String mostrarFormularioNovaFicha(Model model,
                                             @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        Long usuario4Id = 4L;

        List<IngredienteGetDTO> ingredientesDisponiveis = ingredienteService
                .buscarPorStatusEUsuarios(Status.ATIVA, usuarioId, usuario4Id);

        model.addAttribute("categorias", Categoria.values());
        model.addAttribute("ingredientesDisponiveis", ingredientesDisponiveis);


        model.addAttribute("ficha", new FichaTecnicaCreateDTO(null, null,null, null, null, null,
                ATIVA,null ,null,null, new PreparacaoCreateDTO(null,"", null, "", "","",
                null, null, null, null, null), new ArrayList<>(),
                new PerfilNutricionalCreateDTO(null, null, null, null, null,
                        null, null, null, null, null, null,
                        null, null
                )
        ));

        return "Nutri/Ficha/FormInsert";
    }

    @PostMapping("/fichas")
    public String salvarFichaTecnica(
            @ModelAttribute FichaTecnicaCreateDTO fichaTecnicaDTO,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal
    ) {
        Long usuarioId = usuarioPrincipal.getId();
        fichaTecnicaService.create(fichaTecnicaDTO, usuarioId);
        return "redirect:/nutricionista/fichas";
    }


    @GetMapping("/ingredientes")
    public String listarIngredientes(
            Model model,
            String view,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorStatusEUsuario(ATIVA, usuarioId);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("statusAtual", ATIVA);
        model.addAttribute("view", view != null ? view : "meus");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/usuario4")
    public String listarIngredientesDoUsuarioTaco(Model model, String view) {
        List<Ingrediente> ingredientes = ingredienteService.buscarIngredientesDoUsuarioTaco();
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "taco");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/por-nome")
    public String buscarPorNome(@RequestParam String nome, Model model,
                                @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorNome(nome, usuarioId);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "meus");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/por-ptn")
    public String buscarPorPTN(@RequestParam BigDecimal ptn, Model model,
                               @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorPTN(ptn, usuarioId);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "meus");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/por-cho")
    public String buscarPorCHO(@RequestParam BigDecimal cho, Model model,
                               @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorCHO(cho, usuarioId);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "meus");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/por-lip")
    public String buscarPorLIP(@RequestParam BigDecimal lip, Model model,
                               @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorLIP(lip, usuarioId);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "meus");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/por-sodio")
    public String buscarPorSodio(@RequestParam BigDecimal sodio, Model model,
                                 @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorSodio(sodio, usuarioId);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "meus");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/por-gordura-saturada")
    public String buscarPorGorduraSaturada(@RequestParam BigDecimal gorduraSaturada, Model model,
                                           @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorGorduraSaturada(gorduraSaturada, usuarioId);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "meus");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/usuario4/por-nome")
    public String buscarPorNomeUsuario4(@RequestParam String nome, Model model) {
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorNomeUsuario4(nome);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "taco");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/usuario4/por-ptn")
    public String buscarPorPTNUsuario4(@RequestParam BigDecimal ptn, Model model) {
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorPTNUsuario4(ptn);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "taco");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/usuario4/por-cho")
    public String buscarPorCHOUsuario4(@RequestParam BigDecimal cho, Model model) {
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorCHOUsuario4(cho);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "taco");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/usuario4/por-lip")
    public String buscarPorLIPUsuario4(@RequestParam BigDecimal lip, Model model) {
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorLIPUsuario4(lip);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "taco");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/usuario4/por-sodio")
    public String buscarPorSodioUsuario4(@RequestParam BigDecimal sodio, Model model) {
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorSodioUsuario4(sodio);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "taco");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/usuario4/por-gordura-saturada")
    public String buscarPorGorduraSaturadaUsuario4(@RequestParam BigDecimal gorduraSaturada, Model model) {
        List<IngredienteGetDTO> ingredientes = ingredienteService.buscarPorGorduraSaturadaUsuario4(gorduraSaturada);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("view", "taco");
        return "Nutri/Ingrediente/List";
    }

    @GetMapping("/ingredientes/por-status")
    public String buscarPorStatusIngredientes(
            @RequestParam Status status,
            @RequestParam(required = false) String view,
            Model model, @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        List<IngredienteGetDTO> ingredientes;
        if ("taco".equals(view)) {
            // Buscar ingredientes do usuário4 (TACO)
            ingredientes = ingredienteService.buscarPorStatusEUsuario(status, 4L);
        } else {
            // Buscar ingredientes do usuário logado
            Long usuarioId = usuarioPrincipal.getId();
            ingredientes = ingredienteService.buscarPorStatusEUsuario(status, usuarioId);
        }
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("statusAtual", status);
        model.addAttribute("view", view != null ? view : "meus");
        return "Nutri/Ingrediente/List";
    }

    @PostMapping("/ingredientes/atualiza-status/{id}")
    public String atualizaStatus(@PathVariable Long id) {
        ingredienteService.atualizaStatus(id);
        return "redirect:/nutricionista/ingredientes";
    }


    @GetMapping("/ingredientes/novo")
    public String mostrarFormularioCriacao(Model model) {
        model.addAttribute("ingrediente", new IngredienteGetDTO(null,"" , null,  null,  null, null,  null,  null, null));
        return "Nutri/Ingrediente/Form";
    }

    @PostMapping("/ingredientes/novo")
    public String criarIngrediente(@ModelAttribute("ingrediente") IngredienteCreateDTO dto,
                                   @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        ingredienteService.create(dto, usuarioId);
        return "redirect:/nutricionista/ingredientes";
    }

    @GetMapping("/ingredientes/editar/{id}")
    public String mostrarFormularioEdicaoIngredientes(@PathVariable Long id, Model model) {
        IngredienteGetDTO ingrediente = ingredienteService.getIngredienteById(id);
        model.addAttribute("ingrediente", ingrediente);
        return "Nutri/Ingrediente/Form";
    }

    @PostMapping("/ingredientes/editar/{id}")
    public String atualizarIngrediente(@PathVariable Long id,
                                       @ModelAttribute("ingrediente") IngredienteUpdateDTO dto) {
        IngredienteGetDTO dtoId = new IngredienteGetDTO(id, "", null, null, null, null, null, null, null);
        ingredienteService.update(dto, dtoId);
        return "redirect:/nutricionista/ingredientes";
    }

    @GetMapping("/refeicoes")
    public String listarRefeicoes(
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {

        Long usuarioId = usuarioPrincipal.getId();
        model.addAttribute("refeicoes", refeicaoService.buscarPorStatusENutricionista(ATIVA, usuarioId));
        model.addAttribute("statusAtual", ATIVA);
        return "Nutri/Refeicao/List";
    }

    @GetMapping("/refeicoes/por-status")
    public String buscarPorStatusRefeicoes(
            @RequestParam Status status,
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<RefeicaoResponseDTO> refeicoes = refeicaoService.buscarPorStatusENutricionista(status, usuarioId);
        model.addAttribute("refeicoes", refeicoes);
        model.addAttribute("statusAtual", status);
        return "Nutri/Refeicao/List";
    }

    @GetMapping("/refeicoes/pesquisar")
    public String pesquisarPorNome(
            @RequestParam("nome") String nome,
            @RequestParam(value = "status", required = false) Status status,
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long usuarioId = usuarioPrincipal.getId();
        List<RefeicaoResponseDTO> resultados = refeicaoService.buscarPorNomeENutricionista(nome, usuarioId);

        model.addAttribute("refeicoes", resultados);
        model.addAttribute("statusAtual", status != null ? status : Status.ATIVA);
        model.addAttribute("termoBusca", nome);

        return "Nutri/Refeicao/List";
    }

    @PostMapping("/refeicoes/toggle-status/{id}")
    public String toggleStatusRefeicao(@PathVariable Long id, 
                                      @RequestParam(required = false) String currentStatus) {
        refeicaoService.atualizaStatus(id);
        
        // Redireciona mantendo o status atual
        if (currentStatus != null && currentStatus.equals("INATIVA")) {
            return "redirect:/nutricionista/refeicoes/por-status?status=INATIVA";
        } else {
            return "redirect:/nutricionista/refeicoes";
        }
    }

    @GetMapping("/refeicoes/editar/{id}")
    public String mostrarFormularioEdicaoRefeicoes(@PathVariable Long id, Model model) {
        RefeicaoResponseDTO refeicaoResponse = refeicaoService.buscarPorId(id);
        List<FichaTecnicaRefeicaoDTO> fichas = fichaTecnicaService.listarResumo();

        model.addAttribute("refeicaoDTO", new RefeicaoDTO(
                refeicaoResponse.id(),
                refeicaoResponse.nome(),
                refeicaoResponse.kcalTotal(),
                refeicaoResponse.status(),
                refeicaoResponse.fichasTecnicas().stream().map(FichaTecnicaRefeicaoDTO::id).toList()
        ));
        model.addAttribute("fichasTecnicasRefeicao", refeicaoResponse.fichasTecnicas());
        model.addAttribute("fichasTecnicas", fichas);
        return "Nutri/Refeicao/Form";
    }

    @PostMapping("/refeicoes/editar/{id}")
    public String atualizarRefeicao(@PathVariable Long id, @ModelAttribute RefeicaoDTO dto) {
        refeicaoService.update(id, dto);
        return "redirect:/nutricionista/refeicoes";
    }


    @GetMapping("/refeicoes/novo")
    public String mostrarFormularioCriacaoRefeicoes(Model model) {
        List<FichaTecnicaRefeicaoDTO> fichas = fichaTecnicaService.listarResumo();
        RefeicaoDTO refeicaoDTO = new RefeicaoDTO(null, null, "0", Status.ATIVA, new ArrayList<>());
        model.addAttribute("refeicaoDTO", refeicaoDTO);
        model.addAttribute("fichasTecnicas", fichas);
        model.addAttribute("fichasTecnicasRefeicao", new ArrayList<>());
        return "Nutri/Refeicao/Form";
    }

    @PostMapping("/refeicoes/novo")
    public String criarRefeicao(

            @ModelAttribute RefeicaoDTO dto,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal
    ) {
        Long nutricionistaId = usuarioPrincipal.getId();
        refeicaoService.create(dto, nutricionistaId);
        return "redirect:/nutricionista/refeicoes";
    }

    @GetMapping("/fichas/exportar-pdf/{id}")
    public ResponseEntity<ByteArrayResource> exportarFichaTecnica(@PathVariable Long id) {
        try {
            FichaTecnicaGetDTO ficha = fichaTecnicaService.getFichaById(id);
            byte[] pdfBytes = pdfService.generateFichaTecnicaPdf(ficha);

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            HttpHeaders headers = new HttpHeaders();
            String fileName = ficha.preparacao().nome().replaceAll("[^a-zA-Z0-9\\s-]", "").replaceAll("\\s+", "_");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
