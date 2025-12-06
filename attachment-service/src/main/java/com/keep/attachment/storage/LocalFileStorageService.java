package com.keep.attachment.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path rootDir;

    public LocalFileStorageService(
            @Value("${attachments.storage.root-dir:./data/attachments}") String rootDir
    ) {
        this.rootDir = Paths.get(rootDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootDir);
        } catch (IOException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Could not create storage directory", e);
        }
    }

    @Override
    public String store(MultipartFile file, Long userId, Long noteId) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }

        String randomName = UUID.randomUUID().toString().replace("-", "") + extension;
        Path userDir = rootDir.resolve(String.valueOf(userId)).resolve(String.valueOf(noteId));

        try {
            Files.createDirectories(userDir);
            Path target = userDir.resolve(randomName);
            file.transferTo(target.toFile());
            return rootDir.relativize(target).toString().replace("\\", "/");
        } catch (IOException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Failed to store file", e);
        }
    }

    @Override
    public Resource loadAsResource(String storagePath) {
        Path filePath = rootDir.resolve(storagePath).normalize();
        Resource resource = new FileSystemResource(filePath.toFile());
        if (!resource.exists() || !resource.isReadable()) {
            throw new ResponseStatusException(NOT_FOUND, "File not found");
        }
        return resource;
    }

    @Override
    public void delete(String storagePath) {
        Path filePath = rootDir.resolve(storagePath).normalize();
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Failed to delete file", e);
        }
    }
}
