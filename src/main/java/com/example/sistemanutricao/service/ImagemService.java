package com.example.sistemanutricao.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@Service
public class ImagemService {

    private final Path rootLocation = Paths.get("src/main/resources/static/imagens-perfil");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar o diretório de upload", e);
        }
    }

    public String armazenarImagemPerfil(MultipartFile arquivo, String nomeUsuario) {
        try {
            if (arquivo.isEmpty()) {
                throw new RuntimeException("Arquivo vazio");
            }

            String extensao = "";
            String nomeOriginal = arquivo.getOriginalFilename();
            if (nomeOriginal != null && nomeOriginal.contains(".")) {
                extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            }
            String nomeArquivo = nomeUsuario.replaceAll("[^a-zA-Z0-9]", "_") + "_" + UUID.randomUUID().toString().substring(0, 8) + extensao;
            Path destino = this.rootLocation.resolve(nomeArquivo);

            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            return "imagens-perfil/" + nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao armazenar arquivo", e);
        }
    }

    public Resource carregarImagem(String nomeArquivo) {
        try {
            Path arquivo;
            if (nomeArquivo.startsWith("imagens-perfil/")) {
                arquivo = rootLocation.resolve(nomeArquivo.substring("imagens-perfil/".length()));
            } else {
                arquivo = rootLocation.resolve(nomeArquivo);
            }
            
            Resource resource = new UrlResource(arquivo.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Não foi possível ler o arquivo: " + nomeArquivo);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + nomeArquivo, e);
        }
    }

    public void removerImagemPerfil(String caminhoImagem) {
        if (caminhoImagem != null && !caminhoImagem.isEmpty()) {
            try {
                String nomeArquivo = caminhoImagem;
                if (caminhoImagem.startsWith("imagens-perfil/")) {
                    nomeArquivo = caminhoImagem.substring("imagens-perfil/".length());
                }
                
                Path arquivo = rootLocation.resolve(nomeArquivo);
                if (Files.exists(arquivo)) {
                    Files.delete(arquivo);
                }
            } catch (IOException e) {
                System.err.println("Erro ao remover imagem: " + e.getMessage());
            }
        }
    }
}
