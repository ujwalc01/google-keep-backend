package com.keep.reminder.dto;

import jakarta.validation.constraints.Future;

import java.time.Instant;

public class UpdateReminderRequest {

    private Instant remindAt;

    private String message;

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
