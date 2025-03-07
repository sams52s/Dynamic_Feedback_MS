CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       role VARCHAR(100) DEFAULT 'USER' NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       user_name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT NULL,
                       is_deleted BOOLEAN DEFAULT FALSE,
                       deleted_at TIMESTAMP DEFAULT NULL,
                       deleted_by VARCHAR(100) DEFAULT NULL

);

CREATE INDEX idx_users_email ON users(email);

INSERT INTO users (name, user_name, email, password, role)
VALUES
    ('Super', 'DSi', 'dsi@dsinnovators.com',
     '123',
     'SUPER_ADMIN');


CREATE TABLE audit_log (
                           id SERIAL PRIMARY KEY,
                           table_name VARCHAR(255) NOT NULL, -- Name of the affected table
                           record_id INT NOT NULL, -- ID of the affected record
                           action VARCHAR(50) NOT NULL CHECK (action IN ('CREATE', 'UPDATE', 'DELETE')),
                           action_by INT NOT NULL, -- User ID who performed the action
                           action_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for optimized query performance
CREATE INDEX idx_audit_table ON audit_log(table_name);
CREATE INDEX idx_audit_action_by ON audit_log(action_by);

-- Create audit log function
CREATE OR REPLACE FUNCTION log_user_changes() RETURNS TRIGGER AS $$
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

-- Attach trigger to users table for automatic auditing
CREATE TRIGGER users_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON users
    FOR EACH ROW EXECUTE FUNCTION log_user_changes();
