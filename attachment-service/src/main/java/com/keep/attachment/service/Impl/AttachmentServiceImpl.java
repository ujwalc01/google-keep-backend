package com.keep.attachment.service.Impl;

import com.keep.attachment.dto.AttachmentResponse;
import com.keep.attachment.entity.Attachment;
import com.keep.attachment.repository.AttachmentRepository;
import com.keep.attachment.security.JwtUserPrincipal;
import com.keep.attachment.service.AttachmentService;
import com.keep.attachment.storage.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository,
                                 FileStorageService fileStorageService) {
        this.attachmentRepository = attachmentRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public AttachmentResponse uploadAttachment(JwtUserPrincipal principal, Long noteId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "File is empty");
        }

        String storagePath = fileStorageService.store(file, principal.getUserId(), noteId);

        Attachment attachment = Attachment.builder()
                .userId(principal.getUserId())
                .noteId(noteId)
                .fileName(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename())
                .originalFileName(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .storagePath(storagePath)
                .build();

        attachment = attachmentRepository.save(attachment);
        return toResponse(attachment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttachmentResponse> getAttachmentsForNote(JwtUserPrincipal principal, Long noteId) {
        return attachmentRepository
                .findByUserIdAndNoteIdOrderByCreatedAtDesc(principal.getUserId(), noteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Resource loadAttachmentFile(JwtUserPrincipal principal, Long attachmentId) {
        Attachment attachment = findOwnedAttachment(principal, attachmentId);
        return fileStorageService.loadAsResource(attachment.getStoragePath());
    }

    @Override
    public void deleteAttachment(JwtUserPrincipal principal, Long attachmentId) {
        Attachment attachment = findOwnedAttachment(principal, attachmentId);
        fileStorageService.delete(attachment.getStoragePath());
        attachmentRepository.delete(attachment);
    }

    private Attachment findOwnedAttachment(JwtUserPrincipal principal, Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Attachment not found"));

        if (!attachment.getUserId().equals(principal.getUserId())) {
            throw new ResponseStatusException(FORBIDDEN, "Attachment does not belong to user");
        }
        return attachment;
    }

    private AttachmentResponse toResponse(Attachment attachment) {
        AttachmentResponse response = new AttachmentResponse();
        response.setId(attachment.getId());
        response.setNoteId(attachment.getNoteId());
        response.setFileName(attachment.getFileName());
        response.setOriginalFileName(attachment.getOriginalFileName());
        response.setContentType(attachment.getContentType());
        response.setSize(attachment.getSize());
        response.setCreatedAt(attachment.getCreatedAt());
        response.setUpdatedAt(attachment.getUpdatedAt());
        return response;
    }
}
