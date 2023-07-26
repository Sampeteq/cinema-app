create table if not exists bookings_details(
    id bigint primary key generated always as identity not null,
    film_title varchar,
    room_custom_id varchar,
    booking_id bigint,
    foreign key (booking_id) references bookings (id)
)