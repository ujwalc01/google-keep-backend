package com.keep.reminder.controller;

import com.keep.reminder.dto.NotificationResponse;
import com.keep.reminder.security.JwtUserPrincipal;
import com.keep.reminder.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationResponse> getNotifications(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestParam(name = "onlyUnread", defaultValue = "false") boolean onlyUnread
    ) {
        return notificationService.getNotifications(principal, onlyUnread);
    }

    @PostMapping("/{id}/read")
    public void markAsRead(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long id
    ) {
        notificationService.markAsRead(principal, id);
    }
}
