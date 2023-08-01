--liquibase formatted sql
--changeset system:create-table-patient

CREATE TABLE patient(
   id VARCHAR(40) PRIMARY KEY,
   name VARCHAR(40),
   full_name VARCHAR(40)
);