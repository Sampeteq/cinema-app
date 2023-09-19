--liquibase formatted sql
--changeset nofenak:5

create table if not exists users
(
    id       bigint generated always as identity primary key,
    mail     varchar,
    password varchar,
    role     varchar,
    password_reset_token uuid
);