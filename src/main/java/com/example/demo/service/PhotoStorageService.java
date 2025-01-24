package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PhotoStorageService {

    private final String uploadDir = "uploads/";

    public String save(MultipartFile file) throws IOException {
        // Generate a unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Save the file locally
        Path filePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(filePath.getParent());  // Ensure the directories exist
        Files.write(filePath, file.getBytes());  // Write the file to the path

        // Return the relative path or URL
        return "/uploads/" + fileName;
    }
}
