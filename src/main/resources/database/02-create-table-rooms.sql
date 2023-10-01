--liquibase formatted sql
--changeset nofenak:2

create table if not exists rooms
(
    id               varchar,
    rows_number      int,
    row_seats_number int
);