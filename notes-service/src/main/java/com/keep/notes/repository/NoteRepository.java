package com.keep.notes.repository;

import com.keep.notes.entity.Note;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Long> {

    // owner-only methods (for modifications)
    List<Note> findByOwnerIdAndDeletedFalseOrderByUpdatedAtDesc(Long ownerId);

    Optional<Note> findByIdAndOwnerIdAndDeletedFalse(Long id, Long ownerId);

    // visible notes (owner or collaborator)
    @Query("""
           select distinct n
           from Note n
           left join n.collaboratorIds c
           where n.deleted = false
             and (n.ownerId = :userId or c = :userId)
           order by n.updatedAt desc
           """)
    List<Note> findVisibleNotesForUser(@Param("userId") Long userId);

    @Query("""
           select n
           from Note n
           left join n.collaboratorIds c
           where n.deleted = false
             and n.id = :noteId
             and (n.ownerId = :userId or c = :userId)
           """)
    Optional<Note> findVisibleNoteForUser(@Param("noteId") Long noteId,
                                          @Param("userId") Long userId);
}
