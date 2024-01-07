create table if not exists screenings_seats
(
    id           bigint generated always as identity primary key,
    is_free      boolean,
    hall_seat_id bigint,
    screening_id bigint
)