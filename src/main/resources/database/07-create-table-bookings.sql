--liquibase formatted sql
--changeset nofenak:6

create table if not exists bookings
(
    id           bigint generated always as identity primary key,
    status       varchar,
    screening_id bigint,
    seat_id      bigint,
    user_id      bigint,
    foreign key (screening_id) references bookings_screenings (id)
);