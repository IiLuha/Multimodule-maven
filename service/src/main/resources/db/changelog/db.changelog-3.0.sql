--liquibase formatted sql

--changeset kseniaw:1
alter table author
alter column birthday drop not null;