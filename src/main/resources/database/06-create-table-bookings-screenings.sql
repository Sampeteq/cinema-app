create table if not exists bookings_screenings(
    id bigint primary key not null,
    date timestamp,
    film_id bigint,
    room_id bigint
)