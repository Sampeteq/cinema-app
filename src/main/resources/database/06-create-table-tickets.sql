--liquibase formatted sql
--changeset nofenak:6

create table if not exists tickets
(
    id             bigint generated always as identity primary key,
    status         varchar,
    film_title     varchar,
    screening_id   bigint,
    screening_date timestamp,
    room_custom_id varchar,
    row_number     int,
    seat_number    int,
    user_id        bigint
);