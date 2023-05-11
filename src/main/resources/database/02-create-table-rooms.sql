--liquibase formatted sql
--changeset nofenak:2

create table if not exists rooms
(
    id                        bigint generated always as identity primary key,
    custom_id                 varchar,
    rows_quantity             int,
    seats_in_one_row_quantity int
);