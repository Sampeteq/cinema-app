--liquibase formatted sql
--changeset me:3

ALTER TABLE BOOKINGS DROP COLUMN SCREENING_ID;