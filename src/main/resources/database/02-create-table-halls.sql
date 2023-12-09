--liquibase formatted sql
--changeset nofenak:2

create table if not exists halls
(
    id               bigint generated always as identity primary key
);