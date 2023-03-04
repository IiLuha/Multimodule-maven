--liquibase formatted sql

--changeset tsurakov:1
alter table book
add column image Varchar(64);

--changeset tsurakov:2
alter table author
add column image Varchar(64);