create table if not exists rooms_occupations
(
    id       bigint primary key generated always as identity,
    start_at timestamp,
    end_at   timestamp,
    room_id  varchar
);
