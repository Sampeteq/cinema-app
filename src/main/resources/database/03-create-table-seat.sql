--liquibase formatted sql
--changeset sampeteq:3

CREATE TABLE IF NOT EXISTS seat
(
    row_number INTEGER                                 NOT NULL,
    number     INTEGER                                 NOT NULL,
    hall_id    BIGINT                                  NOT NULL,
    CONSTRAINT FK_HALL_SEAT_ON_HALL FOREIGN KEY (hall_id) REFERENCES hall (id)
);