create table if not exists tickets_views(
    id bigint primary key,
    status varchar,
    film_title varchar,
    screening_date timestamp,
    room_custom_id varchar,
    seat_row_number int,
    seat_number int,
    user_id bigint
)