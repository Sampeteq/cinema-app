--liquibase formatted sql
--changeset nofenak:6

create table if not exists bookings
(
    id           bigint generated always as identity primary key,
    status       varchar,
    seat_id      bigint,
    user_id      bigint,
    foreign key (seat_id) references bookings_seats (id),
    foreign key (user_id) references users (id)
);