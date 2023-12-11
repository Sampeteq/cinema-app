create table if not exists screenings_seats
(
    id           bigint primary key,
    row_number   int,
    number       int,
    is_free      boolean,
    screening_id bigint
)