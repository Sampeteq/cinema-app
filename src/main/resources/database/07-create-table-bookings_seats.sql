--liquibase formatted sql
--changeset nofenak:7

create table if not exists bookings_seats(
    id uuid primary key not null,
    is_available boolean,
    time_to_screening_in_hours int
);
alter table bookings drop constraint bookings_seat_id_fkey;
alter table bookings add constraint bookings_seat_id_fkey foreign key (seat_id) REFERENCES bookings_seats (id);

