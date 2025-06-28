package com.example.sistemanutricao.model;

public enum Cargo {
    ADMIN("Admin"),
    PRODUCAO("Produção"),
    NUTRICIONISTA("Nutricionista");

    private final String nome;

    Cargo(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
