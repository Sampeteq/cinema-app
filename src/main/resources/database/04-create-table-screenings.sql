--liquibase formatted sql
--changeset nofenak:3

create table if not exists screenings
(
    id      bigint generated always as identity primary key,
    date    timestamp,
    end_date timestamp,
    film_id bigint,
    hall_id bigint
);