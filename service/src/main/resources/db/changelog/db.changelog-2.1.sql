--liquibase formatted sql

--changeset kseniaw:1
alter table book
add column image Varchar(64);

--changeset kseniaw:2
alter table author
add column image Varchar(64);