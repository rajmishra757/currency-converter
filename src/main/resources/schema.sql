-- Create table api_keys

CREATE TABLE IF NOT EXISTS api_keys (
    id BIGINT PRIMARY KEY,
    key_value VARCHAR(128) NOT NULL UNIQUE,
    owner VARCHAR(64) NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP
);