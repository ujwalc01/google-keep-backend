-- V1__init_notes_schema.sql
-- Initial schema for notes-service: notes, labels, note_labels, note_collaborators

CREATE TABLE IF NOT EXISTS notes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    owner_id BIGINT NOT NULL,
    title VARCHAR(255),
    content LONGTEXT,
    color VARCHAR(50),
    pinned BIT NOT NULL DEFAULT 0,
    archived BIT NOT NULL DEFAULT 0,
    deleted BIT NOT NULL DEFAULT 0,
    reminder_at TIMESTAMP(6) NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_notes PRIMARY KEY (id),
    INDEX idx_notes_owner (owner_id),
    INDEX idx_notes_owner_deleted (owner_id, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS labels (
    id BIGINT NOT NULL AUTO_INCREMENT,
    owner_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    color VARCHAR(50),
    deleted BIT NOT NULL DEFAULT 0,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_labels PRIMARY KEY (id),
    CONSTRAINT uk_label_owner_name UNIQUE (owner_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS note_labels (
    note_id BIGINT NOT NULL,
    label_id BIGINT NOT NULL,
    CONSTRAINT pk_note_labels PRIMARY KEY (note_id, label_id),
    CONSTRAINT fk_note_labels_note FOREIGN KEY (note_id) REFERENCES notes (id) ON DELETE CASCADE,
    CONSTRAINT fk_note_labels_label FOREIGN KEY (label_id) REFERENCES labels (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS note_collaborators (
    note_id BIGINT NOT NULL,
    collaborator_id BIGINT NOT NULL,
    CONSTRAINT pk_note_collaborators PRIMARY KEY (note_id, collaborator_id),
    CONSTRAINT fk_note_collaborators_note FOREIGN KEY (note_id) REFERENCES notes (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
