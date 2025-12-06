package com.keep.reminder.service.Impl;

import com.keep.reminder.dto.CreateReminderRequest;
import com.keep.reminder.dto.ReminderResponse;
import com.keep.reminder.dto.UpdateReminderRequest;
import com.keep.reminder.entity.Reminder;
import com.keep.reminder.entity.ReminderStatus;
import com.keep.reminder.repository.ReminderRepository;
import com.keep.reminder.security.JwtUserPrincipal;
import com.keep.reminder.service.ReminderService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderServiceImpl(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @Override
    public ReminderResponse createReminder(JwtUserPrincipal principal, CreateReminderRequest request) {
        Reminder reminder = Reminder.builder()
                .userId(principal.getUserId())
                .noteId(request.getNoteId())
                .remindAt(request.getRemindAt())
                .status(ReminderStatus.PENDING)
                .message(request.getMessage())
                .build();

        reminder = reminderRepository.save(reminder);
        return toResponse(reminder);
    }

    @Override
    public ReminderResponse updateReminder(JwtUserPrincipal principal, Long id, UpdateReminderRequest request) {
        Reminder reminder = findOwnedReminder(principal, id);

        if (request.getRemindAt() != null) {
            reminder.setRemindAt(request.getRemindAt());
        }
        if (request.getMessage() != null) {
            reminder.setMessage(request.getMessage());
        }

        reminder = reminderRepository.save(reminder);
        return toResponse(reminder);
    }

    @Override
    public void deleteReminder(JwtUserPrincipal principal, Long id) {
        Reminder reminder = findOwnedReminder(principal, id);
        reminder.setStatus(ReminderStatus.CANCELLED);
        reminderRepository.save(reminder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReminderResponse> getRemindersForUser(JwtUserPrincipal principal) {
        return reminderRepository.findByUserIdOrderByRemindAtAsc(principal.getUserId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Reminder findOwnedReminder(JwtUserPrincipal principal, Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Reminder not found"));

        if (!reminder.getUserId().equals(principal.getUserId())) {
            throw new ResponseStatusException(FORBIDDEN, "Reminder does not belong to user");
        }

        return reminder;
    }

    private ReminderResponse toResponse(Reminder reminder) {
        ReminderResponse response = new ReminderResponse();
        response.setId(reminder.getId());
        response.setNoteId(reminder.getNoteId());
        response.setRemindAt(reminder.getRemindAt());
        response.setStatus(reminder.getStatus());
        response.setMessage(reminder.getMessage());
        return response;
    }
}
