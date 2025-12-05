package com.keep.notes.service;

import com.keep.notes.dto.NoteCreateRequest;
import com.keep.notes.dto.NoteResponse;
import com.keep.notes.dto.NoteUpdateRequest;
import java.util.List;

public interface NoteService {

    NoteResponse createNote(Long ownerId, NoteCreateRequest request);

    List<NoteResponse> getNotesForOwner(Long ownerId);

    NoteResponse getNote(Long ownerId, Long noteId);

    NoteResponse updateNote(Long ownerId, Long noteId, NoteUpdateRequest request);

    void deleteNote(Long ownerId, Long noteId);

    NoteResponse setArchived(Long ownerId, Long noteId, boolean archived);

    NoteResponse setPinned(Long ownerId, Long noteId, boolean pinned);

    NoteResponse addLabel(Long ownerId, Long noteId, Long labelId);

    NoteResponse removeLabel(Long ownerId, Long noteId, Long labelId);

    NoteResponse addCollaborator(Long ownerId, Long noteId, Long collaboratorUserId);

    NoteResponse removeCollaborator(Long ownerId, Long noteId, Long collaboratorUserId);
}
