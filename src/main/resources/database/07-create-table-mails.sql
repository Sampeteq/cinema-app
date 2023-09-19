create table if not exists mails
(
    id       bigint generated always as identity primary key,
    receiver varchar,
    subject  varchar,
    text     varchar,
    type     varchar,
    sent_at  timestamp
);
