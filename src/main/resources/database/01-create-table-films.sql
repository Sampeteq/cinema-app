--liquibase formatted sql
--changeset nofenak:1

CREATE TABLE IF NOT EXISTS FILMS
(
    id                  bigint generated always as identity primary key,
    title               varchar,
    category            varchar,
    year                int,
    duration_in_minutes int
)