package com.keep.notes.service.Impl;

import com.keep.notes.dto.LabelResponse;
import com.keep.notes.dto.NoteCreateRequest;
import com.keep.notes.dto.NoteResponse;
import com.keep.notes.dto.NoteUpdateRequest;
import com.keep.notes.entity.Label;
import com.keep.notes.entity.Note;
import com.keep.notes.exception.NotFoundException;
import com.keep.notes.repository.LabelRepository;
import com.keep.notes.repository.NoteRepository;
import com.keep.notes.service.NoteService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final LabelRepository labelRepository;

    public NoteServiceImpl(NoteRepository noteRepository, LabelRepository labelRepository) {
        this.noteRepository = noteRepository;
        this.labelRepository = labelRepository;
    }

    @Override
    @Transactional
    public NoteResponse createNote(Long ownerId, NoteCreateRequest request) {
        Note note = Note.builder()
                .ownerId(ownerId)
                .title(request.getTitle())
                .content(request.getContent())
                .color(request.getColor())
                .pinned(Boolean.TRUE.equals(request.getPinned()))
                .build();
        Note saved = noteRepository.save(note);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponse> getNotesForOwner(Long ownerId) {
        // ownerId here is actually "current user id" — we return all visible notes
        return noteRepository.findVisibleNotesForUser(ownerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NoteResponse getNote(Long ownerId, Long noteId) {
        // allow owner or collaborator to read
        Note note = noteRepository.findVisibleNoteForUser(noteId, ownerId)
                .orElseThrow(() -> new NotFoundException("NOTE_NOT_FOUND", "Note not found"));
        return toResponse(note);
    }

    @Override
    @Transactional
    public NoteResponse updateNote(Long ownerId, Long noteId, NoteUpdateRequest request) {
        // only owner can modify
        Note note = noteRepository.findByIdAndOwnerIdAndDeletedFalse(noteId, ownerId)
                .orElseThrow(() -> new NotFoundException("NOTE_NOT_FOUND", "Note not found"));

        if (request.getTitle() != null) {
            note.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            note.setContent(request.getContent());
        }
        if (request.getColor() != null) {
            note.setColor(request.getColor());
        }
        if (request.getPinned() != null) {
            note.setPinned(request.getPinned());
        }
        if (request.getArchived() != null) {
            note.setArchived(request.getArchived());
        }
        if (request.getDeleted() != null) {
            note.setDeleted(request.getDeleted());
        }

        Note saved = noteRepository.save(note);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteNote(Long ownerId, Long noteId) {
        // only owner can delete
        Note note = noteRepository.findByIdAndOwnerIdAndDeletedFalse(noteId, ownerId)
                .orElseThrow(() -> new NotFoundException("NOTE_NOT_FOUND", "Note not found"));

        note.setDeleted(true);
        noteRepository.save(note);
    }

    @Override
    @Transactional
    public NoteResponse setArchived(Long ownerId, Long noteId, boolean archived) {
        // only owner can archive
        Note note = noteRepository.findByIdAndOwnerIdAndDeletedFalse(noteId, ownerId)
                .orElseThrow(() -> new NotFoundException("NOTE_NOT_FOUND", "Note not found"));

        note.setArchived(archived);
        Note saved = noteRepository.save(note);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public NoteResponse setPinned(Long ownerId, Long noteId, boolean pinned) {
        // only owner can pin
        Note note = noteRepository.findByIdAndOwnerIdAndDeletedFalse(noteId, ownerId)
                .orElseThrow(() -> new NotFoundException("NOTE_NOT_FOUND", "Note not found"));

        note.setPinned(pinned);
        Note saved = noteRepository.save(note);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public NoteResponse addLabel(Long ownerId, Long noteId, Long labelId) {
        // only owner manages labels
        Note note = noteRepository.findByIdAndOwnerIdAndDeletedFalse(noteId, ownerId)
                .orElseThrow(() -> new NotFoundException("NOTE_NOT_FOUND", "Note not found"));

        Label label = labelRepository.findByIdAndOwnerIdAndDeletedFalse(labelId, ownerId)
                .orElseThrow(() -> new NotFoundException("LABEL_NOT_FOUND", "Label not found"));

        note.getLabels().add(label);
        Note saved = noteRepository.save(note);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public NoteResponse removeLabel(Long ownerId, Long noteId, Long labelId) {
        // only owner manages labels
        Note note = noteRepository.findByIdAndOwnerIdAndDeletedFalse(noteId, ownerId)
                .orElseThrow(() -> new NotFoundException("NOTE_NOT_FOUND", "Note not found"));

        Label label = labelRepository.findByIdAndOwnerIdAndDeletedFalse(labelId, ownerId)
                .orElseThrow(() -> new NotFoundException("LABEL_NOT_FOUND", "Label not found"));

        note.getLabels().remove(label);
        Note saved = noteRepository.save(note);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public NoteResponse addCollaborator(Long ownerId, Long noteId, Long collaboratorUserId) {
        // only owner can share
        Note note = noteRepository.findByIdAndOwnerIdAndDeletedFalse(noteId, ownerId)
                .orElseThrow(() -> new NotFoundException("NOTE_NOT_FOUND", "Note not found"));

        if (!ownerId.equals(collaboratorUserId)) {
            note.getCollaboratorIds().add(collaboratorUserId);
        }

        Note saved = noteRepository.save(note);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public NoteResponse removeCollaborator(Long ownerId, Long noteId, Long collaboratorUserId) {
        // only owner can unshare
        Note note = noteRepository.findByIdAndOwnerIdAndDeletedFalse(noteId, ownerId)
                .orElseThrow(() -> new NotFoundException("NOTE_NOT_FOUND", "Note not found"));

        note.getCollaboratorIds().remove(collaboratorUserId);
        Note saved = noteRepository.save(note);
        return toResponse(saved);
    }

    private NoteResponse toResponse(Note note) {
        List<LabelResponse> labelResponses = note.getLabels().stream()
                .filter(l -> !l.isDeleted())
                .map(l -> new LabelResponse(l.getId(), l.getName(), l.getColor()))
                .collect(Collectors.toList());

        List<Long> collaborators = new ArrayList<>(note.getCollaboratorIds());

        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getColor(),
                note.isPinned(),
                note.isArchived(),
                note.isDeleted(),
                note.getCreatedAt(),
                note.getUpdatedAt(),
                labelResponses,
                collaborators
        );
    }
}
