CREATE TABLE comment
(
    id          SERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    feedback_id BIGINT NOT NULL,
    content     TEXT   NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT NULL,
    is_deleted  BOOLEAN   DEFAULT FALSE,
    deleted_at  TIMESTAMP DEFAULT NULL,
    deleted_by  BIGINT    DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (feedback_id) REFERENCES feedback (id) ON DELETE CASCADE
);

CREATE INDEX idx_comment_user_id ON comment (user_id);
CREATE INDEX idx_comment_feedback_id ON comment (feedback_id);

CREATE TABLE approval
(
    id              BIGSERIAL PRIMARY KEY,
    feedback_id     BIGINT  NOT NULL,
    approved_by     BIGINT  NOT NULL,
    approval_status BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP        DEFAULT NULL,
    is_deleted      BOOLEAN          DEFAULT FALSE,
    deleted_at      TIMESTAMP        DEFAULT NULL,
    deleted_by      VARCHAR(100)     DEFAULT NULL,
    FOREIGN KEY (feedback_id) REFERENCES feedback (id) ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_approval_feedback_id ON approval (feedback_id);
CREATE INDEX idx_approval_approved_by ON approval (approved_by);
