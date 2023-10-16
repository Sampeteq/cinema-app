--liquibase formatted sql
--changeset nofenak:6

create table if not exists tickets
(
    id             bigint generated always as identity primary key,
    status         varchar,
    screening_id   bigint,
    row_number     int,
    seat_number    int,
    user_id        bigint
);