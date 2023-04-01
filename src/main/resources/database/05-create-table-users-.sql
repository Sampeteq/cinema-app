--liquibase formatted sql
--changeset nofenak:5

create table if not exists users
(
    username varchar primary key,
    password varchar,
    role     varchar
);