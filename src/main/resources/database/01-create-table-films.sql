--liquibase formatted sql
--changeset nofenak:1

CREATE TABLE IF NOT EXISTS FILMS
(
    id                  uuid primary key,
    title               varchar,
    category            varchar,
    year                int,
    duration_in_minutes int
)