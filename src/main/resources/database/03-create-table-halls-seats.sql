--liquibase formatted sql
--changeset nofenak:4

create table if not exists halls_seats
(
    id           bigint generated always as identity primary key,
    row_number   int,
    number       int,
    hall_id bigint
);