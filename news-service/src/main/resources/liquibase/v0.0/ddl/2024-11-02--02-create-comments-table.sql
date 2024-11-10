--liquibase formatted sql
--changeset Minich:2
CREATE TABLE IF NOT EXISTS comments
(
    id         UUID PRIMARY KEY,
    created_by UUID,
    updated_by UUID,
    created_at TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at TIMESTAMP            DEFAULT now(),
    text       TEXT        NOT NULL,
    username   VARCHAR(40) NOT NULL,
    news_id    UUID REFERENCES news (id)
);