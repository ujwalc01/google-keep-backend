-- V1__init_reminder_schema.sql
-- Initial schema for reminder-service: reminders + notifications

CREATE TABLE IF NOT EXISTS reminders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    note_id BIGINT NOT NULL,
    remind_at TIMESTAMP(6) NOT NULL,
    message VARCHAR(255) NOT NULL,
    active BIT NOT NULL DEFAULT 1,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_reminders PRIMARY KEY (id),
    INDEX idx_reminders_user_active (user_id, active),
    INDEX idx_reminders_user_time (user_id, remind_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    reminder_id BIGINT NOT NULL,
    note_id BIGINT NOT NULL,
    message VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    read_at TIMESTAMP(6) NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id),
    INDEX idx_notifications_user_status (user_id, status, created_at),
    CONSTRAINT fk_notifications_reminder FOREIGN KEY (reminder_id) REFERENCES reminders (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
