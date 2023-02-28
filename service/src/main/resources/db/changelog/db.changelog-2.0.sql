--liquibase formatted sql

--changeset kseniaw:1
alter table users
add column image Varchar(64);