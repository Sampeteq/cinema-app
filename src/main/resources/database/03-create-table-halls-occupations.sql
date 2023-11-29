create table if not exists halls_occupations
(
    id       bigint primary key generated always as identity,
    start_at timestamp,
    end_at   timestamp,
    hall_id  bigint,
    screening_id bigint
);
