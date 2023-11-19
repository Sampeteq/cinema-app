--liquibase formatted sql
--changeset nofenak:3

create table if not exists screenings
(
    id      bigint generated always as identity primary key,
    date    timestamp,
    film_id varchar,
    hall_id varchar
);