--liquibase formatted sql
--changeset nofenak:2

create table if not exists halls
(
    id               varchar,
    rows_number      int,
    seats_number_in_one_row int
);