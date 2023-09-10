--liquibase formatted sql
--changeset nofenak:4

create table if not exists seats
(
    id           bigint generated always as identity primary key,
    row_number   int,
    number       int,
    is_free      boolean,
    screening_id bigint,
    booking_id   bigint,
    foreign key (screening_id) references bookings_screenings (id)
);