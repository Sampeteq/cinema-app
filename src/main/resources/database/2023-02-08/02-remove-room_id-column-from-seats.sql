--liquibase formatted sql
--changeset me:2

ALTER TABLE SEATS DROP COLUMN ROOM_ID;