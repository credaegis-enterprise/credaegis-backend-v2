CREATE TABLE notifications
(
    id      VARCHAR(255) NOT NULL,
    message VARCHAR(255) NOT NULL,
    type    VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);