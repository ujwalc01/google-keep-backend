package com.keep.attachment.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String store(MultipartFile file, Long userId, Long noteId);

    Resource loadAsResource(String storagePath);

    void delete(String storagePath);
}
