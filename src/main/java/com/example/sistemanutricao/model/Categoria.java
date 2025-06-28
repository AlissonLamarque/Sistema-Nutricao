package com.example.sistemanutricao.model;

import java.util.Arrays;

public enum Categoria {
    PRATOPRINCIPAL("Prato Principal"),
    COMPLEMENTO("Complemento"),
    SOBREMESA("Sobremesa"),
    BEBIDA("Bebida"),
    SALADA("Salada");

    private final String nome;

    Categoria(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public static Categoria fromNome(String nome) {
        return Arrays.stream(values())
                .filter(c -> c.nome.equals(nome))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Categoria inválida: " + nome));
    }

}
