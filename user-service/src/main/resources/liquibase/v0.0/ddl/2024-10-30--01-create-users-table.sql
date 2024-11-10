--liquibase formatted sql
--changeset Minich:1
CREATE TABLE IF NOT EXISTS users
(
    id         UUID PRIMARY KEY,
    created_by UUID,
    updated_by UUID REFERENCES users (id),
    created_at TIMESTAMP           NOT NULL DEFAULT now(),
    updated_at TIMESTAMP                    DEFAULT now(),
    first_name VARCHAR(40)         NOT NULL,
    last_name  VARCHAR(50)         NOT NULL,
    password   VARCHAR(100)        NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    status     VARCHAR(10)         NOT NULL
);