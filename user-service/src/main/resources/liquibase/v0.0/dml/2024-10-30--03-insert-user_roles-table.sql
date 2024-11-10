--liquibase formatted sql
--changeset Minich:3
INSERT INTO user_roles (user_id, role_id)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '73c65923-b5b1-42df-bd99-299180f287e0'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2512c298-6a1d-48d7-a12d-b51069aceb08'),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'f5b50fda-f157-4a8b-948c-6705206c81c6');