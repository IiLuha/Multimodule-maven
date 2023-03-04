--liquibase formatted sql

--changeset tsurakov:1
alter table users
add column image Varchar(64);