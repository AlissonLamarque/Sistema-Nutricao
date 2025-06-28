package com.example.sistemanutricao.model;

import jakarta.persistence.*;

import java.util.Objects;


@Entity
public class FichasPorRefeicao {

    @EmbeddedId
    private FichasPorRefeicaoId id;

    @ManyToOne
    @MapsId("refeicaoId")
    @JoinColumn(name = "refeicao_id")
    private Refeicao refeicao;

    @ManyToOne
    @MapsId("fichaTecnicaId")
    @JoinColumn(name = "ficha_tecnica_id")
    private FichaTecnica fichaTecnica;

    public FichasPorRefeicao() {
    }

    public FichasPorRefeicao(Refeicao refeicao, FichaTecnica fichaTecnica) {
        this.refeicao = Objects.requireNonNull(refeicao);
        this.fichaTecnica = Objects.requireNonNull(fichaTecnica);
        this.id = new FichasPorRefeicaoId(refeicao.getId(), fichaTecnica.getId());
    }

    public FichasPorRefeicaoId getId() {
        return id;
    }

    public void setId(FichasPorRefeicaoId id) {
        this.id = id;
    }

    public Refeicao getRefeicao() {
        return refeicao;
    }

    public void setRefeicao(Refeicao refeicao) {
        this.refeicao = refeicao;
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
        FichasPorRefeicao that = (FichasPorRefeicao) o;
        return Objects.equals(id, that.id) && Objects.equals(refeicao, that.refeicao) && Objects.equals(fichaTecnica, that.fichaTecnica);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, refeicao, fichaTecnica);
    }

    @Override
    public String toString() {
        return "FichasPorRefeicao{" +
                "id=" + id +
                ", refeicao=" + refeicao +
                ", fichaTecnica=" + fichaTecnica +
                '}';
    }
}