--liquibase formatted sql
--changeset Minich:1

CREATE TABLE IF NOT EXISTS news
(
    id         UUID PRIMARY KEY,
    created_by UUID,
    updated_by UUID,
    created_at TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at TIMESTAMP             DEFAULT now(),
    title      VARCHAR(100) NOT NULL,
    text       VARCHAR(500) NOT NULL,
    id_author  UUID         NOT NULL
);