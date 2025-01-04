

ALTER TABLE users
    ADD COLUMN password_reset_token VARCHAR(255) NULL;

ALTER TABLE users
    ADD COLUMN password_reset_token_creation_time datetime NULL;

