--liquibase formatted sql
--changeset sampeteq:6

CREATE TABLE IF NOT EXISTS "user"
(
    id                   UUID                                    NOT NULL,
    mail                 VARCHAR(255)                            NOT NULL,
    password             VARCHAR(255)                            NOT NULL,
    role                 VARCHAR(255)                            NOT NULL,
    password_reset_token UUID,
    CONSTRAINT pk_user PRIMARY KEY (id)
);