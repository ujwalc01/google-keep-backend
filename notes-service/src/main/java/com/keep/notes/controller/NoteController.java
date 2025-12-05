package com.keep.notes.controller;

import com.keep.notes.dto.NoteCreateRequest;
import com.keep.notes.dto.NoteResponse;
import com.keep.notes.dto.NoteUpdateRequest;
import com.keep.notes.security.JwtUserPrincipal;
import com.keep.notes.service.NoteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Valid @RequestBody NoteCreateRequest request
    ) {
        NoteResponse response = noteService.createNote(principal.getUserId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getNotes(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        List<NoteResponse> notes = noteService.getNotesForOwner(principal.getUserId());
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNoteById(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id
    ) {
        NoteResponse response = noteService.getNote(principal.getUserId(), id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody NoteUpdateRequest request
    ) {
        NoteResponse response = noteService.updateNote(principal.getUserId(), id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id
    ) {
        noteService.deleteNote(principal.getUserId(), id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<NoteResponse> archiveNote(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id
    ) {
        NoteResponse response = noteService.setArchived(principal.getUserId(), id, true);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/unarchive")
    public ResponseEntity<NoteResponse> unarchiveNote(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id
    ) {
        NoteResponse response = noteService.setArchived(principal.getUserId(), id, false);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/pin")
    public ResponseEntity<NoteResponse> pinNote(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id
    ) {
        NoteResponse response = noteService.setPinned(principal.getUserId(), id, true);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/unpin")
    public ResponseEntity<NoteResponse> unpinNote(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id
    ) {
        NoteResponse response = noteService.setPinned(principal.getUserId(), id, false);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/labels/{labelId}")
    public ResponseEntity<NoteResponse> addLabelToNote(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id,
            @PathVariable Long labelId
    ) {
        NoteResponse response = noteService.addLabel(principal.getUserId(), id, labelId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/labels/{labelId}")
    public ResponseEntity<NoteResponse> removeLabelFromNote(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id,
            @PathVariable Long labelId
    ) {
        NoteResponse response = noteService.removeLabel(principal.getUserId(), id, labelId);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/collaborators/{collaboratorId}")
    public ResponseEntity<NoteResponse> addCollaborator(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id,
            @PathVariable Long collaboratorId
    ) {
        NoteResponse response = noteService.addCollaborator(principal.getUserId(), id, collaboratorId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/collaborators/{collaboratorId}")
    public ResponseEntity<NoteResponse> removeCollaborator(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id,
            @PathVariable Long collaboratorId
    ) {
        NoteResponse response = noteService.removeCollaborator(principal.getUserId(), id, collaboratorId);
        return ResponseEntity.ok(response);
    }
}
