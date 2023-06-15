--liquibase formatted sql
--changeset nofenak:3

create table if not exists screenings
(
    id      bigint generated always as identity primary key,
    date    timestamp with time zone,
    end_date timestamp with time zone,
    film_id bigint,
    room_id bigint,
    foreign key (film_id) references films (id),
    foreign key (room_id) references rooms (id)
);