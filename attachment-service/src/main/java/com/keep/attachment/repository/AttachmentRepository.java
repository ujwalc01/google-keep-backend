package com.keep.attachment.repository;

import com.keep.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByUserIdAndNoteIdOrderByCreatedAtDesc(Long userId, Long noteId);
}
