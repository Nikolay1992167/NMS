--liquibase formatted sql
--changeset Minich:3
CREATE TABLE IF NOT EXISTS user_roles
(
    user_id UUID NOT NULL REFERENCES users (id),
    role_id UUID NOT NULL REFERENCES roles (id),
    PRIMARY KEY (user_id, role_id)
);