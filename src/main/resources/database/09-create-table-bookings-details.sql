create table if not exists bookings_details(
    id bigint primary key generated always as identity not null,
    film_title varchar,
    room_custom_id varchar,
    seat_row_number int,
    seat_number int,
    booking_id bigint,
    foreign key (booking_id) references bookings (id)
)