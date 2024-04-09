--liquibase formatted sql
--changeset sampeteq:7

CREATE TABLE IF NOT EXISTS ticket
(
    id           UUID   NOT NULL,
    version      BIGINT,
    screening_id UUID,
    row_number   INTEGER,
    number       INTEGER,
    user_id      UUID,
    CONSTRAINT pk_ticket PRIMARY KEY (id),
    CONSTRAINT FK_TICKET_ON_SCREENING FOREIGN KEY (screening_id) REFERENCES screening (id),
    CONSTRAINT FK_TICKET_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id)
);