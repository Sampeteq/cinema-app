--liquibase formatted sql
--changeset nofenak:5

create table if not exists users
(
    mail     varchar primary key,
    password varchar,
    role     varchar
);