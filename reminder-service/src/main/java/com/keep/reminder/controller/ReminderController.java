package com.keep.reminder.controller;

import com.keep.reminder.dto.CreateReminderRequest;
import com.keep.reminder.dto.ReminderResponse;
import com.keep.reminder.dto.UpdateReminderRequest;
import com.keep.reminder.security.JwtUserPrincipal;
import com.keep.reminder.service.ReminderService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public ReminderResponse createReminder(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Valid @RequestBody CreateReminderRequest request
    ) {
        return reminderService.createReminder(principal, request);
    }

    @PutMapping("/{id}")
    public ReminderResponse updateReminder(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody UpdateReminderRequest request
    ) {
        return reminderService.updateReminder(principal, id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteReminder(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id
    ) {
        reminderService.deleteReminder(principal, id);
    }

    @GetMapping
    public List<ReminderResponse> getReminders(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        return reminderService.getRemindersForUser(principal);
    }
}
