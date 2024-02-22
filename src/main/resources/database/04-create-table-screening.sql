--liquibase formatted sql
--changeset sampeteq:4

CREATE TABLE IF NOT EXISTS screening
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    date     TIMESTAMP WITHOUT TIME ZONE,
    film_id  BIGINT,
    hall_id  BIGINT,
    CONSTRAINT pk_screening PRIMARY KEY (id),
    CONSTRAINT FK_SCREENING_ON_FILM FOREIGN KEY (film_id) REFERENCES film (id)
);