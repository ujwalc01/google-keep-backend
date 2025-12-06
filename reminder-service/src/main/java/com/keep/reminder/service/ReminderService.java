package com.keep.reminder.service;

import com.keep.reminder.dto.CreateReminderRequest;
import com.keep.reminder.dto.ReminderResponse;
import com.keep.reminder.dto.UpdateReminderRequest;
import com.keep.reminder.security.JwtUserPrincipal;

import java.util.List;

public interface ReminderService {

    ReminderResponse createReminder(JwtUserPrincipal principal, CreateReminderRequest request);

    ReminderResponse updateReminder(JwtUserPrincipal principal, Long id, UpdateReminderRequest request);

    void deleteReminder(JwtUserPrincipal principal, Long id);

    List<ReminderResponse> getRemindersForUser(JwtUserPrincipal principal);
}
