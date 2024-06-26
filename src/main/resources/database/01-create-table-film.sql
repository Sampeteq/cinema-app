--liquibase formatted sql
--changeset sampeteq:1

CREATE TABLE IF NOT EXISTS film
(
    id                  UUID                                    NOT NULL,
    title               VARCHAR(255)                            NOT NULL,
    category            VARCHAR(255)                            NOT NULL,
    year                INTEGER                                 NOT NULL,
    duration_in_minutes INTEGER                                 NOT NULL,
    CONSTRAINT pk_film PRIMARY KEY (id)
);