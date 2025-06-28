package com.example.sistemanutricao.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class PerfilNutricional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal vtc;

    private BigDecimal kcalPTN;

    private BigDecimal kcalCHO;

    private BigDecimal kcalLIP;

    private BigDecimal gramasPTN;

    private BigDecimal gramasCHO;

    private BigDecimal gramasLIP;

    private BigDecimal gramasSodio;

    private BigDecimal gramasSaturada;

    private BigDecimal porcentPTN;

    private BigDecimal porcentCHO;

    private BigDecimal porcentLIP;

    @OneToMany(mappedBy = "perfilNutricional", cascade = CascadeType.ALL)
    private List<IngredientesPorFicha> ingredientesPorFicha;

    @OneToOne(mappedBy = "perfilNutricional", cascade = CascadeType.ALL)
    private FichaTecnica fichaTecnica;

    public PerfilNutricional() {
    }

    public PerfilNutricional(Long id) {
        this.id = id;
    }

    public PerfilNutricional(Long id, BigDecimal vtc, BigDecimal kcalPTN, BigDecimal kcalCHO, BigDecimal kcalLIP, BigDecimal gramasPTN,
                             BigDecimal gramasCHO, BigDecimal gramasLIP, BigDecimal gramasSodio, BigDecimal gramasSaturada,
                             BigDecimal porcentPTN, BigDecimal porcentCHO, BigDecimal porcentLIP, List<IngredientesPorFicha> ingredientesPorFicha,
                             FichaTecnica fichaTecnica) {
        this.id = id;
        this.vtc = vtc;
        this.kcalPTN = kcalPTN;
        this.kcalCHO = kcalCHO;
        this.kcalLIP = kcalLIP;
        this.gramasPTN = gramasPTN;
        this.gramasCHO = gramasCHO;
        this.gramasLIP = gramasLIP;
        this.gramasSodio = gramasSodio;
        this.gramasSaturada = gramasSaturada;
        this.porcentPTN = porcentPTN;
        this.porcentCHO = porcentCHO;
        this.porcentLIP = porcentLIP;
        this.ingredientesPorFicha = ingredientesPorFicha;
        this.fichaTecnica = fichaTecnica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getVtc() {
        return vtc;
    }

    public void setVtc(BigDecimal vtc) {
        this.vtc = vtc;
    }

    public BigDecimal getKcalPTN() {
        return kcalPTN;
    }

    public void setKcalPTN(BigDecimal kcalPTN) {
        this.kcalPTN = kcalPTN;
    }

    public BigDecimal getKcalCHO() {
        return kcalCHO;
    }

    public void setKcalCHO(BigDecimal kcalCHO) {
        this.kcalCHO = kcalCHO;
    }

    public BigDecimal getKcalLIP() {
        return kcalLIP;
    }

    public void setKcalLIP(BigDecimal kcalLIP) {
        this.kcalLIP = kcalLIP;
    }

    public BigDecimal getGramasPTN() {
        return gramasPTN;
    }

    public void setGramasPTN(BigDecimal gramasPTN) {
        this.gramasPTN = gramasPTN;
    }

    public BigDecimal getGramasCHO() {
        return gramasCHO;
    }

    public void setGramasCHO(BigDecimal gramasCHO) {
        this.gramasCHO = gramasCHO;
    }

    public BigDecimal getGramasLIP() {
        return gramasLIP;
    }

    public void setGramasLIP(BigDecimal gramasLIP) {
        this.gramasLIP = gramasLIP;
    }

    public BigDecimal getGramasSodio() {
        return gramasSodio;
    }

    public void setGramasSodio(BigDecimal gramasSodio) {
        this.gramasSodio = gramasSodio;
    }

    public BigDecimal getGramasSaturada() {
        return gramasSaturada;
    }

    public void setGramasSaturada(BigDecimal gramasSaturada) {
        this.gramasSaturada = gramasSaturada;
    }

    public BigDecimal getPorcentPTN() {
        return porcentPTN;
    }

    public void setPorcentPTN(BigDecimal porcentPTN) {
        this.porcentPTN = porcentPTN;
    }

    public BigDecimal getPorcentCHO() {
        return porcentCHO;
    }

    public void setPorcentCHO(BigDecimal porcentCHO) {
        this.porcentCHO = porcentCHO;
    }

    public BigDecimal getPorcentLIP() {
        return porcentLIP;
    }

    public void setPorcentLIP(BigDecimal porcentLIP) {
        this.porcentLIP = porcentLIP;
    }

    public List<IngredientesPorFicha> getIngredientesPorFicha() {
        return ingredientesPorFicha;
    }

    public void setIngredientesPorFicha(List<IngredientesPorFicha> ingredientesPorFicha) {
        this.ingredientesPorFicha = ingredientesPorFicha;
    }

    public FichaTecnica getFichaTecnica() {
        return fichaTecnica;
    }

    public void setFichaTecnica(FichaTecnica fichaTecnica) {
        this.fichaTecnica = fichaTecnica;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PerfilNutricional that = (PerfilNutricional) o;
        return Objects.equals(id, that.id) && Objects.equals(vtc, that.vtc) && Objects.equals(kcalPTN, that.kcalPTN) && Objects.equals(kcalCHO, that.kcalCHO) && Objects.equals(kcalLIP, that.kcalLIP) && Objects.equals(gramasPTN, that.gramasPTN) && Objects.equals(gramasCHO, that.gramasCHO) && Objects.equals(gramasLIP, that.gramasLIP) && Objects.equals(gramasSodio, that.gramasSodio) && Objects.equals(gramasSaturada, that.gramasSaturada) && Objects.equals(porcentPTN, that.porcentPTN) && Objects.equals(porcentCHO, that.porcentCHO) && Objects.equals(porcentLIP, that.porcentLIP) && Objects.equals(ingredientesPorFicha, that.ingredientesPorFicha) && Objects.equals(fichaTecnica, that.fichaTecnica);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vtc, kcalPTN, kcalCHO, kcalLIP, gramasPTN, gramasCHO, gramasLIP, gramasSodio, gramasSaturada, porcentPTN, porcentCHO, porcentLIP, ingredientesPorFicha, fichaTecnica);
    }

    @Override
    public String toString() {
        return "PerfilNutricional{" +
                "id=" + id +
                ", vtc=" + vtc +
                ", kcalPTN=" + kcalPTN +
                ", kcalCHO=" + kcalCHO +
                ", kcalLIP=" + kcalLIP +
                ", gramasPTN=" + gramasPTN +
                ", gramasCHO=" + gramasCHO +
                ", gramasLIP=" + gramasLIP +
                ", gramasSodio=" + gramasSodio +
                ", gramasSaturada=" + gramasSaturada +
                ", porcentPTN=" + porcentPTN +
                ", porcentCHO=" + porcentCHO +
                ", porcentLIP=" + porcentLIP +
                ", ingredientesPorFicha=" + ingredientesPorFicha +
                ", fichaTecnica=" + fichaTecnica +
                '}';
    }
}
