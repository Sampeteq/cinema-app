--liquibase formatted sql
--changeset nofenak:7
alter table if exists rooms drop column if exists number;
alter table if exists rooms add column if not exists custom_id varchar(255)