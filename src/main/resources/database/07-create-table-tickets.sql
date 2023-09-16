--liquibase formatted sql
--changeset nofenak:6

create table if not exists tickets
(
    id           bigint generated always as identity primary key,
    status       varchar,
    seat_id      bigint,
    user_id      bigint,
    foreign key (seat_id) references seats (id)
);