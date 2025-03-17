CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100)        NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    role       VARCHAR(50)         NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100)                 DEFAULT NULL,
    deleted_at TIMESTAMP                    DEFAULT NULL,
    is_deleted BOOLEAN                      DEFAULT FALSE,
    deleted_by VARCHAR(100)                 DEFAULT NULL,
    updated_at TIMESTAMP                    DEFAULT NULL,
    updated_by VARCHAR(100)                 DEFAULT NULL
);

CREATE INDEX idx_users_email ON users (email);

INSERT INTO users (name, email, password, role)
VALUES ('Super Admin',
        'dsi@dsinnovators.com',
        '$2a$10$ExampleHashedPassword',
        'SUPER_ADMIN');

CREATE TABLE audit_log
(
    id         SERIAL PRIMARY KEY,
    table_name VARCHAR(255) NOT NULL,
    record_id  INT          NOT NULL,
    action     VARCHAR(50)  NOT NULL CHECK (action IN ('CREATE', 'UPDATE', 'DELETE')),
    action_by  INT          NOT NULL,
    action_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_table ON audit_log (table_name);
CREATE INDEX idx_audit_action_by ON audit_log (action_by);

CREATE OR REPLACE FUNCTION log_user_changes() RETURNS TRIGGER AS
$$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        INSERT INTO audit_log (table_name, record_id, action, action_by)
        VALUES ('users', NEW.id, 'CREATE', NEW.id);
        RETURN NEW;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO audit_log (table_name, record_id, action, action_by)
        VALUES ('users', NEW.id, 'UPDATE', NEW.id);
        RETURN NEW;
    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO audit_log (table_name, record_id, action, action_by)
        VALUES ('users', OLD.id, 'DELETE', OLD.id);
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER users_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION log_user_changes();
