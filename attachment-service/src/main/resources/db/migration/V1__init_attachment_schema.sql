-- V1__init_attachment_schema.sql
-- Initial schema for attachment-service: attachments

CREATE TABLE IF NOT EXISTS attachments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    note_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(255),
    size BIGINT NOT NULL,
    storage_path VARCHAR(512) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_attachments PRIMARY KEY (id),
    INDEX idx_attachments_user_note (user_id, note_id),
    INDEX idx_attachments_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
