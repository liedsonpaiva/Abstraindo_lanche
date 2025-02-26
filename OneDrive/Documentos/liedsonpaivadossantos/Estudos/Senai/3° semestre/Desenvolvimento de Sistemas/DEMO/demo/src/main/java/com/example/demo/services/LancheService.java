package com.example.demo.services;

import com.example.demo.entities.Lanche;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class LancheService {
    private String filePath = "C:\\Users\\aluno.fsa\\ImagensLancheDestino\\";
    private Map<Integer, Lanche> lancheMap = new HashMap<>();

    public Lanche getById(int id) {
        return null;
    }

    public boolean exists(int id) {
        return true;
    }

    private String getFileExtension(Path path) {
        String filename = path.getFileName().toString();
        int lastDotIndex = filename.lastIndexOf('.');

        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }

        return filename.substring(lastDotIndex + 1);
    }

    public boolean salvar(Lanche lanche) {
        Path path = Paths.get(lanche.getImagem());

        Path destinationPath = Paths.get(String.format("%s%d.%s", filePath, lanche.getCodigo(), getFileExtension(path)));

        if (Files.exists(path)) {
            try {
                Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                lanche.setImagem(destinationPath.toString());
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        return false;
    }

    public void excluir(int id) {
        if (exists(id)) {
            Lanche lanche = lancheMap.get(id);
            Path imagePath = Paths.get(lanche.getImagem());
            try {
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            lancheMap.remove(id);
        }
    }

    public void atualizar(int id, Lanche novoLanche) {
        if (exists(id)) {
            excluir(id);
            salvar(novoLanche);
        }
    }
}
