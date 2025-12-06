package com.keep.reminder.repository;

import com.keep.reminder.entity.Reminder;
import com.keep.reminder.entity.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByUserIdOrderByRemindAtAsc(Long userId);

    List<Reminder> findTop50ByStatusAndRemindAtLessThanEqualOrderByRemindAtAsc(
            ReminderStatus status,
            Instant remindAt
    );
}
