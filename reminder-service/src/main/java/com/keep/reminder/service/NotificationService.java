package com.keep.reminder.service;

import com.keep.reminder.dto.NotificationResponse;
import com.keep.reminder.entity.Reminder;
import com.keep.reminder.security.JwtUserPrincipal;

import java.util.List;

public interface NotificationService {

    void createNotificationForReminder(Reminder reminder);

    List<NotificationResponse> getNotifications(JwtUserPrincipal principal, boolean onlyUnread);

    void markAsRead(JwtUserPrincipal principal, Long notificationId);
}
