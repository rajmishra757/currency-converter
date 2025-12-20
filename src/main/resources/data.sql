-- Inserting seed keys into api_keys table

INSERT INTO api_keys (id, key_value, owner, active, created_at)
VALUES (1, 'demo-active-key-123', 'demo-user', TRUE, CURRENT_TIMESTAMP);

INSERT INTO api_keys (id, key_value, owner, active, created_at, revoked_at)
VALUES (2, 'demo-revoked-key-123', 'old-user', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);