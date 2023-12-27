create table if not exists screenings_seats
(
    id           bigint generated always as identity primary key,
    row_number   int,
    number       int,
    is_free      boolean,
    screening_id bigint
)