package com.keep.attachment.controller;

import com.keep.attachment.dto.AttachmentResponse;
import com.keep.attachment.security.JwtUserPrincipal;
import com.keep.attachment.service.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AttachmentResponse uploadAttachment(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestParam("noteId") Long noteId,
            @RequestPart("file") MultipartFile file
    ) {
        return attachmentService.uploadAttachment(principal, noteId, file);
    }

    @GetMapping("/note/{noteId}")
    public List<AttachmentResponse> getAttachmentsForNote(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long noteId
    ) {
        return attachmentService.getAttachmentsForNote(principal, noteId);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadAttachment(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id
    ) {
        Resource resource = attachmentService.loadAttachmentFile(principal, id);

        String filename = resource.getFilename();
        if (filename == null) {
            filename = "attachment";
        }
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public void deleteAttachment(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id
    ) {
        attachmentService.deleteAttachment(principal, id);
    }
}
