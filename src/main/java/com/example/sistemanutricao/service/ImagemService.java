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

    // Pasta dentro de resources/static para armazenar as imagens
    private final Path rootLocation = Paths.get("src/main/resources/static/imagens-perfil");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar o diretório de upload", e);
        }
    }

    /**
     * Armazena uma imagem de perfil com o nome do usuário
     * @param arquivo Arquivo de imagem
     * @param nomeUsuario Nome do usuário para incluir no nome do arquivo
     * @return Nome do arquivo salvo
     */
    public String armazenarImagemPerfil(MultipartFile arquivo, String nomeUsuario) {
        try {
            if (arquivo.isEmpty()) {
                throw new RuntimeException("Arquivo vazio");
            }

            // Obter extensão do arquivo original
            String extensao = "";
            String nomeOriginal = arquivo.getOriginalFilename();
            if (nomeOriginal != null && nomeOriginal.contains(".")) {
                extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            }

            // Criar nome do arquivo com nome do usuário e UUID para evitar conflitos
            String nomeArquivo = nomeUsuario.replaceAll("[^a-zA-Z0-9]", "_") + "_" + UUID.randomUUID().toString().substring(0, 8) + extensao;
            Path destino = this.rootLocation.resolve(nomeArquivo);

            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            // Retornar o caminho relativo para acessar via web
            return "imagens-perfil/" + nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao armazenar arquivo", e);
        }
    }

    /**
     * Método legado para compatibilidade - mantém o comportamento anterior
     */
    public String armazenarImagem(MultipartFile arquivo) {
        try {
            if (arquivo.isEmpty()) {
                throw new RuntimeException("Arquivo vazio");
            }

            String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
            Path destino = this.rootLocation.resolve(nomeArquivo);

            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            return nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao armazenar arquivo", e);
        }
    }

    public Resource carregarImagem(String nomeArquivo) {
        try {
            // Se o nome do arquivo já inclui o caminho completo, usamos diretamente
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

    /**
     * Remove uma imagem de perfil
     * @param caminhoImagem Caminho da imagem a ser removida
     */
    public void removerImagemPerfil(String caminhoImagem) {
        if (caminhoImagem != null && !caminhoImagem.isEmpty()) {
            try {
                // Se o caminho já inclui "imagens-perfil/", removemos para obter apenas o nome do arquivo
                String nomeArquivo = caminhoImagem;
                if (caminhoImagem.startsWith("imagens-perfil/")) {
                    nomeArquivo = caminhoImagem.substring("imagens-perfil/".length());
                }
                
                Path arquivo = rootLocation.resolve(nomeArquivo);
                if (Files.exists(arquivo)) {
                    Files.delete(arquivo);
                }
            } catch (IOException e) {
                // Log do erro mas não falha a operação
                System.err.println("Erro ao remover imagem: " + e.getMessage());
            }
        }
    }
}
