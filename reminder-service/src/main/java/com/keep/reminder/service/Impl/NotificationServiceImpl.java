package com.keep.reminder.service.Impl;

import com.keep.reminder.dto.NotificationResponse;
import com.keep.reminder.entity.Notification;
import com.keep.reminder.entity.NotificationStatus;
import com.keep.reminder.entity.Reminder;
import com.keep.reminder.repository.NotificationRepository;
import com.keep.reminder.security.JwtUserPrincipal;
import com.keep.reminder.service.NotificationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void createNotificationForReminder(Reminder reminder) {
        String message = reminder.getMessage();
        if (message == null || message.isBlank()) {
            message = "Reminder for note " + reminder.getNoteId();
        }

        Notification notification = Notification.builder()
                .userId(reminder.getUserId())
                .noteId(reminder.getNoteId())
                .message(message)
                .status(NotificationStatus.UNREAD)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(JwtUserPrincipal principal, boolean onlyUnread) {
        List<Notification> notifications;
        if (onlyUnread) {
            notifications = notificationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(
                    principal.getUserId(),
                    NotificationStatus.UNREAD
            );
        } else {
            notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(principal.getUserId());
        }

        return notifications.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void markAsRead(JwtUserPrincipal principal, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Notification not found"));

        if (!notification.getUserId().equals(principal.getUserId())) {
            throw new ResponseStatusException(FORBIDDEN, "Notification does not belong to user");
        }

        notification.setStatus(NotificationStatus.READ);
        notification.setReadAt(Instant.now());
        notificationRepository.save(notification);
    }

    private NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setNoteId(notification.getNoteId());
        response.setMessage(notification.getMessage());
        response.setStatus(notification.getStatus());
        response.setCreatedAt(notification.getCreatedAt());
        response.setReadAt(notification.getReadAt());
        return response;
    }
}
