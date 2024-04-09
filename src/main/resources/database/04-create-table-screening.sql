--liquibase formatted sql
--changeset sampeteq:4

CREATE TABLE IF NOT EXISTS screening
(
    id       UUID NOT NULL,
    date     TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    film_id  UUID,
    hall_id  UUID,
    CONSTRAINT pk_screening PRIMARY KEY (id)
);