package com.example.sistemanutricao.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

@Entity
public class Preparacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated
    private Categoria categoria;

    private Integer numero;

    private String tempoPreparo;

    @Column(length = 1000)
    private String equipamentos;

    @Column(length = 1000)
    private String modoPreparo;

    private BigDecimal qntdAgua;

    private BigDecimal porcentAgua;

    private BigDecimal fcc;

    private BigDecimal rendimento;

    @OneToOne(mappedBy = "preparacao", cascade = CascadeType.ALL)
    private FichaTecnica fichaTecnica;

    public Preparacao() {
    }

    public Preparacao(Long id) {
        this.id = id;
    }

    public Preparacao(Long id, String nome, Integer numero, String tempoPreparo, String equipamentos,
                      String modoPreparo, BigDecimal porcentAgua, BigDecimal qntdAgua,
                      BigDecimal fcc, BigDecimal rendimento, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.numero = numero;
        this.tempoPreparo = tempoPreparo;
        this.equipamentos = equipamentos;
        this.modoPreparo = modoPreparo;
        this.porcentAgua = porcentAgua;
        this.qntdAgua = qntdAgua;
        this.fcc = fcc;
        this.rendimento = rendimento;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Integer  getNumero() {
        return numero;
    }

    public void setNumero(Integer  numero) {
        this.numero = numero;
    }

    public String getTempoPreparo() {
        return tempoPreparo;
    }

    public void setTempoPreparo(String tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }

    public String getEquipamentos() {
        return equipamentos;
    }

    public void setEquipamentos(String equipamentos) {
        this.equipamentos = equipamentos;
    }

    public String getModoPreparo() {
        return modoPreparo;
    }

    public void setModoPreparo(String modoPreparo) {
        this.modoPreparo = modoPreparo;
    }

    public BigDecimal getQntdAgua() {
        return qntdAgua;
    }

    public void setQntdAgua(BigDecimal qntdAgua) {
        this.qntdAgua = qntdAgua;
    }

    public BigDecimal getPorcentAgua() {
        return porcentAgua;
    }

    public void setPorcentAgua(BigDecimal porcentAgua) {
        this.porcentAgua = porcentAgua;
    }

    public BigDecimal getFcc() {
        return fcc;
    }

    public void setFcc(BigDecimal fcc) {
        this.fcc = fcc;
    }

    public BigDecimal getRendimento() {
        return rendimento;
    }

    public void setRendimento(BigDecimal rendimento) {
        this.rendimento = rendimento;
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
        Preparacao that = (Preparacao) o;
        return numero == that.numero && Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && categoria == that.categoria && Objects.equals(tempoPreparo, that.tempoPreparo) && Objects.equals(equipamentos, that.equipamentos) && Objects.equals(modoPreparo, that.modoPreparo) && Objects.equals(qntdAgua, that.qntdAgua) && Objects.equals(porcentAgua, that.porcentAgua) && Objects.equals(fcc, that.fcc) && Objects.equals(rendimento, that.rendimento) && Objects.equals(fichaTecnica, that.fichaTecnica);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, categoria, numero, tempoPreparo, equipamentos, modoPreparo, qntdAgua, porcentAgua, fcc, rendimento, fichaTecnica);
    }

    @Override
    public String toString() {
        return "Preparacao{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoria=" + categoria +
                ", numero=" + numero +
                ", tempoPreparo=" + tempoPreparo +
                ", equipamentos=" + equipamentos +
                ", modoPreparo=" + modoPreparo +
                ", qntdAgua=" + qntdAgua +
                ", porcentAgua=" + porcentAgua +
                ", fcc=" + fcc +
                ", rendimento=" + rendimento +
                ", fichaTecnica=" + (fichaTecnica != null ? fichaTecnica.getId() : "null") +
                '}';
    }
}
