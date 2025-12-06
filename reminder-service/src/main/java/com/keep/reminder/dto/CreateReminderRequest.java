package com.keep.reminder.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class CreateReminderRequest {

    @NotNull
    private Long noteId;

    @NotNull
    @Future
    private Instant remindAt;

    private String message;

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Instant getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(Instant remindAt) {
        this.remindAt = remindAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
