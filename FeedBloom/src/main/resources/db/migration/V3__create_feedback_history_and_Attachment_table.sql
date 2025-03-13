CREATE TABLE feedback_history
(
    id                 BIGSERIAL PRIMARY KEY,
    feedback_id        BIGINT NOT NULL,
    changed_by         BIGINT NOT NULL,
    change_description TEXT   NOT NULL,
    created_at         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    DEFAULT NULL,
    is_deleted         BOOLEAN      DEFAULT FALSE,
    deleted_at         TIMESTAMP    DEFAULT NULL,
    deleted_by         VARCHAR(100) DEFAULT NULL,
    FOREIGN KEY (feedback_id) REFERENCES feedback (id) ON DELETE CASCADE,
    FOREIGN KEY (changed_by) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_feedback_history_changed_by ON feedback_history (changed_by);
CREATE INDEX idx_feedback_history_feedback_id ON feedback_history (feedback_id);

CREATE TABLE feedback_attachment
(
    id             BIGSERIAL PRIMARY KEY,
    feedback_id    BIGINT NOT NULL,
    attachment_url TEXT   NOT NULL,
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    DEFAULT NULL,
    is_deleted     BOOLEAN      DEFAULT FALSE,
    deleted_at     TIMESTAMP    DEFAULT NULL,
    deleted_by     VARCHAR(100) DEFAULT NULL,
    FOREIGN KEY (feedback_id) REFERENCES feedback (id) ON DELETE CASCADE
);

CREATE INDEX idx_feedback_attachment_feedback_id ON feedback_attachment (feedback_id);
