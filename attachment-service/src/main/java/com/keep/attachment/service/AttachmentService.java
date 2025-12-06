package com.keep.attachment.service;

import com.keep.attachment.dto.AttachmentResponse;
import com.keep.attachment.security.JwtUserPrincipal;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {

    AttachmentResponse uploadAttachment(JwtUserPrincipal principal, Long noteId, MultipartFile file);

    List<AttachmentResponse> getAttachmentsForNote(JwtUserPrincipal principal, Long noteId);

    Resource loadAttachmentFile(JwtUserPrincipal principal, Long attachmentId);

    void deleteAttachment(JwtUserPrincipal principal, Long attachmentId);
}
