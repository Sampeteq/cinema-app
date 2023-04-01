--liquibase formatted sql
--changeset nofenak:2

create table if not exists rooms
(
    id                        uuid primary key,
    number                    int,
    rows_quantity             int,
    seats_in_one_row_quantity int
);