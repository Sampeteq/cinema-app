create table if not exists tickets_screenings(
    id bigint primary key not null,
    date timestamp,
    film_title varchar,
    room_custom_id varchar
)