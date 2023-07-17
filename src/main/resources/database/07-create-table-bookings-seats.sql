create table if not exists bookings_seats(
    id bigint primary key not null,
    row_number int,
    number int,
    screening_id int,
    foreign key (screening_id) references bookings_screenings(id)
)