--liquibase formatted sql
--changeset sampeteq:2

CREATE TABLE IF NOT EXISTS hall
(
    id UUID NOT NULL,
    CONSTRAINT pk_hall PRIMARY KEY (id)
);