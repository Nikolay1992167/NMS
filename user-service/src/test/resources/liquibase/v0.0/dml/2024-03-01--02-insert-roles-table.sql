--liquibase formatted sql
--changeset Minich:2
INSERT INTO roles (id, name)
VALUES ('73c65923-b5b1-42df-bd99-299180f287e0', 'ADMIN'),
       ('2512c298-6a1d-48d7-a12d-b51069aceb08', 'JOURNALIST'),
       ('f5b50fda-f157-4a8b-948c-6705206c81c6', 'SUBSCRIBER');