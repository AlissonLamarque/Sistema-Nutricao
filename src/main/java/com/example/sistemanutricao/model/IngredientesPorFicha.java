package com.example.sistemanutricao.model;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class IngredientesPorFicha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medidaCaseira;

    private BigDecimal pb;

    private BigDecimal pl;

    private BigDecimal fc;

    private BigDecimal custoUsado;

    private BigDecimal custoKG;

    // Valores nutricionais calculados com base no PL
    private BigDecimal ptnCalculado;
    private BigDecimal choCalculado;
    private BigDecimal lipCalculado;
    private BigDecimal sodioCalculado;
    private BigDecimal gorduraSaturadaCalculada;

    @ManyToOne
    @JoinColumn(name = "fichaTecnica_id", nullable = false)
    private FichaTecnica fichaTecnica;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Ingrediente ingrediente;

    @ManyToOne
    @JoinColumn(name = "perfil_nutricional_id", nullable = false)
    private PerfilNutricional perfilNutricional;

    public IngredientesPorFicha() {
    }

    public IngredientesPorFicha(Long id, String medidaCaseria, BigDecimal pb, BigDecimal pl, BigDecimal fc,
                                BigDecimal custoUsado, BigDecimal custoKG, FichaTecnica fichaTecnica, Ingrediente ingrediente,
                                PerfilNutricional perfilNutricional) {
        this.id = id;
        this.medidaCaseira = medidaCaseria;
        this.pb = pb;
        this.pl = pl;
        this.fc = fc;
        this.custoUsado = custoUsado;
        this.custoKG = custoKG;
        this.fichaTecnica = fichaTecnica;
        this.ingrediente = ingrediente;
        this.perfilNutricional = perfilNutricional;
    }

    public IngredientesPorFicha(Long id, String medidaCaseria, BigDecimal pb, BigDecimal pl, BigDecimal fc,
                                BigDecimal custoUsado, BigDecimal custoKG, BigDecimal ptnCalculado, BigDecimal choCalculado,
                                BigDecimal lipCalculado, BigDecimal sodioCalculado, BigDecimal gorduraSaturadaCalculada,
                                FichaTecnica fichaTecnica, Ingrediente ingrediente, PerfilNutricional perfilNutricional) {
        this.id = id;
        this.medidaCaseira = medidaCaseria;
        this.pb = pb;
        this.pl = pl;
        this.fc = fc;
        this.custoUsado = custoUsado;
        this.custoKG = custoKG;
        this.ptnCalculado = ptnCalculado;
        this.choCalculado = choCalculado;
        this.lipCalculado = lipCalculado;
        this.sodioCalculado = sodioCalculado;
        this.gorduraSaturadaCalculada = gorduraSaturadaCalculada;
        this.fichaTecnica = fichaTecnica;
        this.ingrediente = ingrediente;
        this.perfilNutricional = perfilNutricional;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedidaCaseria() {
        return medidaCaseira;
    }

    public void setMedidaCaseria(String medidaCaseria) {
        this.medidaCaseira = medidaCaseria;
    }

    public BigDecimal getPb() {
        return pb;
    }

    public void setPb(BigDecimal pb) {
        this.pb = pb;
    }

    public BigDecimal getPl() {
        return pl;
    }

    public void setPl(BigDecimal pl) {
        this.pl = pl;
    }

    public BigDecimal getFc() {
        return fc;
    }

    public void setFc(BigDecimal fc) {
        this.fc = fc;
    }

    public BigDecimal getCustoUsado() {
        return custoUsado;
    }

    public void setCustoUsado(BigDecimal custoUsado) {
        this.custoUsado = custoUsado;
    }

    public BigDecimal getCustoKG() {
        return custoKG;
    }

    public void setCustoKG(BigDecimal custoKG) {
        this.custoKG = custoKG;
    }

    public BigDecimal getPtnCalculado() {
        return ptnCalculado;
    }

    public void setPtnCalculado(BigDecimal ptnCalculado) {
        this.ptnCalculado = ptnCalculado;
    }

    public BigDecimal getChoCalculado() {
        return choCalculado;
    }

    public void setChoCalculado(BigDecimal choCalculado) {
        this.choCalculado = choCalculado;
    }

    public BigDecimal getLipCalculado() {
        return lipCalculado;
    }

    public void setLipCalculado(BigDecimal lipCalculado) {
        this.lipCalculado = lipCalculado;
    }

    public BigDecimal getSodioCalculado() {
        return sodioCalculado;
    }

    public void setSodioCalculado(BigDecimal sodioCalculado) {
        this.sodioCalculado = sodioCalculado;
    }

    public BigDecimal getGorduraSaturadaCalculada() {
        return gorduraSaturadaCalculada;
    }

    public void setGorduraSaturadaCalculada(BigDecimal gorduraSaturadaCalculada) {
        this.gorduraSaturadaCalculada = gorduraSaturadaCalculada;
    }

    public FichaTecnica getFichaTecnica() {
        return fichaTecnica;
    }

    public void setFichaTecnica(FichaTecnica fichaTecnica) {
        this.fichaTecnica = fichaTecnica;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public PerfilNutricional getPerfilNutricional() {
        return perfilNutricional;
    }

    public void setPerfilNutricional(PerfilNutricional perfilNutricional) {
        this.perfilNutricional = perfilNutricional;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IngredientesPorFicha that = (IngredientesPorFicha) o;
        return Objects.equals(id, that.id) && Objects.equals(medidaCaseira, that.medidaCaseira) && Objects.equals(pb, that.pb) && Objects.equals(pl, that.pl) && Objects.equals(fc, that.fc) && Objects.equals(custoUsado, that.custoUsado) && Objects.equals(custoKG, that.custoKG) && Objects.equals(ptnCalculado, that.ptnCalculado) && Objects.equals(choCalculado, that.choCalculado) && Objects.equals(lipCalculado, that.lipCalculado) && Objects.equals(sodioCalculado, that.sodioCalculado) && Objects.equals(gorduraSaturadaCalculada, that.gorduraSaturadaCalculada) && Objects.equals(fichaTecnica, that.fichaTecnica) && Objects.equals(ingrediente, that.ingrediente) && Objects.equals(perfilNutricional, that.perfilNutricional);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, medidaCaseira, pb, pl, fc, custoUsado, custoKG, ptnCalculado, choCalculado, lipCalculado, sodioCalculado, gorduraSaturadaCalculada, fichaTecnica, ingrediente, perfilNutricional);
    }

    @Override
    public String toString() {
        return "IngredientesPorFicha{" +
                "id=" + id +
                ", medidaCaseria='" + medidaCaseira + '\'' +
                ", pb=" + pb +
                ", pl=" + pl +
                ", fc=" + fc +
                ", custoUsado=" + custoUsado +
                ", custoKG=" + custoKG +
                ", ptnCalculado=" + ptnCalculado +
                ", choCalculado=" + choCalculado +
                ", lipCalculado=" + lipCalculado +
                ", sodioCalculado=" + sodioCalculado +
                ", gorduraSaturadaCalculada=" + gorduraSaturadaCalculada +
                ", fichaTecnica=" + fichaTecnica +
                ", ingrediente=" + ingrediente +
                ", perfilNutricional=" + perfilNutricional +
                '}';
    }
}
