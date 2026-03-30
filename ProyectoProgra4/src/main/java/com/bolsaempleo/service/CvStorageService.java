package com.bolsaempleo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class CvStorageService {

    private final Path uploadDir;

    public CvStorageService(@Value("${app.cv.upload-dir}") String uploadDirPath) throws IOException {
        this.uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }

    public String store(MultipartFile file, Long oferenteId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo CV no puede estar vacío.");
        }
        String original = file.getOriginalFilename();
        String ext = (original != null && original.contains("."))
                ? original.substring(original.lastIndexOf('.'))
                : ".pdf";
        if (!ext.equalsIgnoreCase(".pdf")) {
            throw new IllegalArgumentException("Solo se aceptan archivos PDF.");
        }

        String filename = oferenteId + "_" + UUID.randomUUID() + ext;
        Path target = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "uploads/cv/" + filename;
    }

    public Path load(String relativePath) {
        String filename = Paths.get(relativePath).getFileName().toString();
        return uploadDir.resolve(filename);
    }
}
