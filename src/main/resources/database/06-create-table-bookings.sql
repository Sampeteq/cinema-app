--liquibase formatted sql
--changeset nofenak:6

create table if not exists bookings
(
    id           bigint generated always as identity primary key,
    status       varchar,
    seat_id      bigint,
    username     varchar,
    foreign key (seat_id) references seats (id),
    foreign key (username) references users (username)
);