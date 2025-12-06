package com.keep.reminder.repository;

import com.keep.reminder.entity.Notification;
import com.keep.reminder.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserIdAndStatusOrderByCreatedAtDesc(
            Long userId,
            NotificationStatus status
    );
}
