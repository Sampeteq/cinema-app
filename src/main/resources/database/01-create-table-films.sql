--liquibase formatted sql
--changeset nofenak:1

create table if not exists films
(
    id                  bigint generated always as identity primary key,
    title               varchar,
    category            varchar,
    year                int,
    duration_in_minutes int
)