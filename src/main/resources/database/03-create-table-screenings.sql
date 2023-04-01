--liquibase formatted sql
--changeset nofenak:3

create table if not exists screenings
(
    id      uuid primary key,
    min_age int,
    date    timestamp with time zone,
    film_id uuid,
    room_id uuid,
    foreign key (film_id) references films (id),
    foreign key (room_id) references rooms (id)
);