CREATE TABLE projects (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100)                 DEFAULT NULL,
    deleted_at TIMESTAMP                    DEFAULT NULL,
    is_deleted BOOLEAN                      DEFAULT FALSE,
    deleted_by VARCHAR(100)                 DEFAULT NULL,
    updated_at TIMESTAMP                    DEFAULT NULL,
    updated_by VARCHAR(100)                 DEFAULT NULL
);

CREATE TABLE feedback (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    project_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    category VARCHAR(50) NOT NULL DEFAULT 'IMPROVEMENT',
    priority VARCHAR(50) NOT NULL DEFAULT 'LOW',
    created_at TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100)                 DEFAULT NULL,
    deleted_at TIMESTAMP                    DEFAULT NULL,
    is_deleted BOOLEAN                      DEFAULT FALSE,
    deleted_by VARCHAR(100)                 DEFAULT NULL,
    updated_at TIMESTAMP                    DEFAULT NULL,
    updated_by VARCHAR(100)                 DEFAULT NULL,
    CONSTRAINT fk_feedback_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_feedback_project FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE
);

CREATE INDEX idx_feedback_user_id ON feedback (user_id);
CREATE INDEX idx_feedback_project_id ON feedback (project_id);

CREATE OR REPLACE FUNCTION log_feedback_changes() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        INSERT INTO audit_log (table_name, record_id, action, action_by)
        VALUES ('feedback', NEW.id, 'CREATE', NEW.user_id);
        RETURN NEW;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO audit_log (table_name, record_id, action, action_by)
        VALUES ('feedback', NEW.id, 'UPDATE', NEW.user_id);
        RETURN NEW;
    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO audit_log (table_name, record_id, action, action_by)
        VALUES ('feedback', OLD.id, 'DELETE', OLD.user_id);
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER feedback_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON feedback
    FOR EACH ROW
EXECUTE FUNCTION log_feedback_changes();
