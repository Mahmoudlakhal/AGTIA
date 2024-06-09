package com.example.springsecurity.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Service
public class StorageService {

    private final Path rootLocation = Paths.get("springsecurity-service/upload-dir");


    public String store(MultipartFile file , int TARGET_WIDTH , int TARGET_HEIGHT) {
        try {
            // Générer un nom de fichier aléatoire
            String fileName = Integer.toString(new Random().nextInt(1000000000));
            // Extraire l'extension du fichier
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            // Construire le nom de fichier original
            String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.'));
            String originalFilename = name + fileName + ext;

            // Lire l'image depuis le flux d'entrée du fichier MultipartFile
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            // Redimensionner l'image
            BufferedImage resizedImage = resizeImage(originalImage, TARGET_WIDTH, TARGET_HEIGHT);

            // Enregistrer l'image redimensionnée dans le répertoire de stockage
            Path destinationFile = this.rootLocation.resolve(originalFilename);
            ImageIO.write(resizedImage, ext.substring(1), destinationFile.toFile());

            return originalFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store the image.", e);
        }
    }


    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }



    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        // Créer une nouvelle image avec les dimensions cibles
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        // Appliquer le redimensionnement
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }




    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}