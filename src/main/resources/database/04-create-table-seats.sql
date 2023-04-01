--liquibase formatted sql
--changeset nofenak:4

create table if not exists seats
(
    id           uuid primary key,
    row_number   int,
    number       int,
    status       varchar,
    screening_id uuid,
    foreign key (screening_id) references screenings (id)
);