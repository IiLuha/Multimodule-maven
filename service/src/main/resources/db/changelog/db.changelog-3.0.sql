--liquibase formatted sql

--changeset tsurakov:1
alter table author
alter column birthday drop not null;