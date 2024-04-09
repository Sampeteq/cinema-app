--liquibase formatted sql
--changeset sampeteq:8

CREATE TABLE mail
(
    UUID     BIGINT  NOT NULL,
    receiver varchar NOT NULL,
    subject  varchar NOT NULL,
    content  varchar NOT NULL,
    sent_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL
)