CREATE TABLE IF NOT EXISTS user_table (
    id BIGSERIAL PRIMARY KEY,
    date_time TIMESTAMP NOT NULL
);

CREATE INDEX idx_user_table_date_time ON user_table (date_time);