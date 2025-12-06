package com.keep.reminder.schedular;

import com.keep.reminder.entity.Reminder;
import com.keep.reminder.entity.ReminderStatus;
import com.keep.reminder.repository.ReminderRepository;
import com.keep.reminder.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class ReminderScheduler {

    private final ReminderRepository reminderRepository;
    private final NotificationService notificationService;

    public ReminderScheduler(ReminderRepository reminderRepository,
                             NotificationService notificationService) {
        this.reminderRepository = reminderRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelayString = "${reminder.scheduler.fixed-delay-ms:60000}")
    @Transactional
    public void processDueReminders() {
        Instant now = Instant.now();
        List<Reminder> dueReminders =
                reminderRepository.findTop50ByStatusAndRemindAtLessThanEqualOrderByRemindAtAsc(
                        ReminderStatus.PENDING, now
                );

        for (Reminder reminder : dueReminders) {
            notificationService.createNotificationForReminder(reminder);
            reminder.setStatus(ReminderStatus.TRIGGERED);
            reminderRepository.save(reminder);
        }
    }
}
