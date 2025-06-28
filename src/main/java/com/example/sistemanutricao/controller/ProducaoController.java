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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sistemanutricao.model.Status;
import static com.example.sistemanutricao.model.Status.ATIVA;
import static com.example.sistemanutricao.model.StatusCriacao.COMPLETA;
import com.example.sistemanutricao.record.FichaTecnicaDTO.FichaTecnicaComTagDTO;
import com.example.sistemanutricao.record.FichaTecnicaDTO.FichaTecnicaGetDTO;
import com.example.sistemanutricao.record.RefeicaoDTO.RefeicaoResponseDTO;
import com.example.sistemanutricao.security.UsuarioSecurity;
import com.example.sistemanutricao.service.FichaTecnicaService;
import com.example.sistemanutricao.service.PdfService;
import com.example.sistemanutricao.service.RefeicaoService;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/producao")
@CrossOrigin("*")
public class ProducaoController {

    private final FichaTecnicaService fichaTecnicaService;
    private final RefeicaoService refeicaoService;
    private final PdfService pdfService;

    public ProducaoController(FichaTecnicaService fichaTecnicaService,
                                   RefeicaoService refeicaoService,
                                   PdfService pdfService) {
        this.fichaTecnicaService = fichaTecnicaService;
        this.refeicaoService = refeicaoService;
        this.pdfService = pdfService;
    }

    @GetMapping
    public String get() {
        return "Producao/Home";
    }

    @GetMapping("/fichas")
    public String listarTodasFichas(
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {

        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorStatusEEstabelecimento(ATIVA, estabelecimentoId, COMPLETA);

        model.addAttribute("fichas", fichas);
        model.addAttribute("statusAtual", ATIVA);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }
    @GetMapping("/fichas/custoPerCapita")
    public String buscarPorCustoPerCapita(
            @RequestParam BigDecimal custoPerCapita,
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorCustPerCapitaEEstabelecimento(custoPerCapita, estabelecimentoId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }

    @GetMapping("/fichas/custoTotal")
    public String buscarPorCustoTotal(
            @RequestParam BigDecimal custoTotal,
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorCustoTotalEEstabelecimento(custoTotal, estabelecimentoId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }

    @GetMapping("/fichas/por-nome")
    public String buscarPorNomePreparacao(@RequestParam String nome, Model model,
                                          @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorNomePreparacaoEEstabelecimento(nome, estabelecimentoId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }

    @GetMapping("/fichas/por-rendimento")
    public String buscarPorRendimento(@RequestParam BigDecimal rendimento, Model model,
                                      @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorRendimentoPreparacaoEEstabelecimento(rendimento, estabelecimentoId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }

    @GetMapping("/fichas/por-numero")
    public String buscarPorNumero(@RequestParam Integer numero, Model model,
                                  @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorNumeroPreparacaoEEstabelecimento(numero, estabelecimentoId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }

    @GetMapping("/fichas/por-vtc")
    public String buscarPorVtc(@RequestParam BigDecimal vtc, Model model,
                               @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorVtcPerfilNutricionalEEstabelecimento(vtc, estabelecimentoId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }

    @GetMapping("/fichas/por-gramas-ptn")
    public String buscarPorGramasPTN(@RequestParam BigDecimal gramasPTN, Model model,
                                     @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorGramasPTNPerfilNutricionalEEstabelecimento(gramasPTN, estabelecimentoId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }

    @GetMapping("/fichas/por-gramas-cho")
    public String buscarPorGramasCHO(@RequestParam BigDecimal gramasCHO, Model model,
                                     @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorGramasCHOPerfilNutricionalEEstabelecimento(gramasCHO, estabelecimentoId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }

    @GetMapping("/fichas/por-gramas-lip")
    public String buscarPorGramasLIP(@RequestParam BigDecimal gramasLIP, Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorGramasLIPPerfilNutricionalEEstabelecimento(gramasLIP, estabelecimentoId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }

    @GetMapping("/fichas/por-gramas-sodio")
    public String buscarPorGramasSodio(@RequestParam BigDecimal gramasSodio, Model model,
                                       @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorGramasSodioPerfilNutricionalEEstabelecimento(gramasSodio, estabelecimentoId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }

    @GetMapping("/fichas/por-gramas-saturada")
    public String buscarPorGramasSaturada(@RequestParam BigDecimal gramasSaturada, Model model,
                                          @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorGramasSaturadaPerfilNutricionalEEstabelecimento(gramasSaturada, estabelecimentoId);
        model.addAttribute("fichas", fichas);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }

    @GetMapping("/fichas/por-tag")
    public String buscarFichasPorTag(
            @RequestParam String campo,
            @RequestParam String tag,
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        try {
            Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
            List<FichaTecnicaComTagDTO> fichas = fichaTecnicaService.buscarPorTagEstabelecimento(campo, tag, estabelecimentoId);
            model.addAttribute("fichas", fichas);
            model.addAttribute("pesquisaPorTag", true);
            model.addAttribute("isComTagDTO", true);
            return "Producao/Ficha/List";
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error in buscarFichasPorTag (Producao): " + e.getMessage());
            e.printStackTrace();
            
            // Return empty list and error message
            model.addAttribute("fichas", new ArrayList<>());
            model.addAttribute("error", "Erro ao buscar fichas por tag: " + e.getMessage());
            model.addAttribute("pesquisaPorTag", true);
            model.addAttribute("isComTagDTO", true);
            return "Producao/Ficha/List";
        }
    }

    @GetMapping("/fichas/por-categoria")
    public String buscarPorCategoriaPreparacao(
            @RequestParam(name = "categoria") String nomeCategoria,
            Model model) {
        List<FichaTecnicaGetDTO> fichas = fichaTecnicaService.buscarPorCategoriaPreparacao(nomeCategoria);
        model.addAttribute("fichas", fichas);
        model.addAttribute("isComTagDTO", false);
        return "Producao/Ficha/List";
    }

    @GetMapping("/fichas/{id}")
    public String verFichaDetalhes(
            @PathVariable Long id,
            Model model) {
        FichaTecnicaGetDTO ficha = fichaTecnicaService.getFichaById(
                id
        );
        model.addAttribute("ficha", ficha);
        return "Producao/Ficha/Detail";
    }

    @GetMapping("/refeicoes")
    public String listarRefeicoes(Model model,
                                  @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        model.addAttribute("refeicoes", refeicaoService.buscarPorStatusEEstabelecimento(ATIVA, estabelecimentoId));
        model.addAttribute("statusAtual", ATIVA);
        return "Producao/ListRefeicao";
    }

    @GetMapping("/refeicoes/pesquisar")
    public String pesquisarPorNome(
            @RequestParam("nome") String nome,
            @RequestParam(value = "status", required = false) Status status,
            Model model,
            @AuthenticationPrincipal UsuarioSecurity usuarioPrincipal) {
        Long estabelecimentoId = usuarioPrincipal.getUsuario().getEstabelecimento().getId();
        List<RefeicaoResponseDTO> resultados = refeicaoService.buscarPorNomeEEstabelecimento(nome, estabelecimentoId);

        model.addAttribute("refeicoes", resultados);
        model.addAttribute("statusAtual", status != null ? status : Status.ATIVA);
        model.addAttribute("termoBusca", nome);

        return "Producao/ListRefeicao";
    }

    @GetMapping("/fichas/exportar-pdf/{id}")
    public ResponseEntity<ByteArrayResource> exportarPdf(@PathVariable Long id) {
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
