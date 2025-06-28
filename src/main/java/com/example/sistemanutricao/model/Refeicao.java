package com.example.sistemanutricao.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Refeicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String kcalTotal;

    private Status status;

    @ManyToOne
    @JoinColumn(name = "nutricionista_id")
    private Usuario nutricionista;

    @OneToMany(mappedBy = "refeicao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FichasPorRefeicao> fichasPorRefeicao = new ArrayList<>();

    public Refeicao() {
    }

    public Refeicao(Long id, String nome, String kcalTotal, Status status, Usuario nutricionista, List<FichasPorRefeicao> fichasPorRefeicao) {
        this.id = id;
        this.nome = nome;
        this.kcalTotal = kcalTotal;
        this.status = status;
        this.nutricionista = nutricionista;
        this.fichasPorRefeicao = fichasPorRefeicao;
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

    public String getKcalTotal() {
        return kcalTotal;
    }

    public void setKcalTotal(String kcalTotal) {
        this.kcalTotal = kcalTotal;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Usuario getNutricionista() {
        return nutricionista;
    }

    public void setNutricionista(Usuario nutricionista) {
        this.nutricionista = nutricionista;
    }

    public List<FichasPorRefeicao> getFichasPorRefeicao() {
        return fichasPorRefeicao;
    }

    public void setFichasPorRefeicao(List<FichasPorRefeicao> fichasPorRefeicao) {
        this.fichasPorRefeicao = fichasPorRefeicao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Refeicao refeicao = (Refeicao) o;
        return Objects.equals(id, refeicao.id) && Objects.equals(nome, refeicao.nome) && Objects.equals(kcalTotal, refeicao.kcalTotal) && status == refeicao.status && Objects.equals(nutricionista, refeicao.nutricionista) && Objects.equals(fichasPorRefeicao, refeicao.fichasPorRefeicao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, kcalTotal, status, nutricionista, fichasPorRefeicao);
    }

    @Override
    public String toString() {
        return "Refeicao{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", kcalTotal='" + kcalTotal + '\'' +
                ", status=" + status +
                ", nutricionista=" + (nutricionista != null ? nutricionista.getId() : "null") +
                ", fichasPorRefeicao=" + (fichasPorRefeicao != null ? fichasPorRefeicao.size() + " items" : "null") +
                '}';
    }
}
