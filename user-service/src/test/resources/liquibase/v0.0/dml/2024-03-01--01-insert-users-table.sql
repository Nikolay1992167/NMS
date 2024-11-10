--liquibase formatted sql
--changeset Minich:1
INSERT INTO users (id, updated_by, first_name, last_name, password, email, status)
VALUES ('44212253-a305-4495-9982-45e833aa74ac', NULL, 'Alex', 'Dronov', '$2a$10$3z6TvAEO2fwLedqVihxZDeQQM.BFCqIc5fP78YUUlL8BcVaPUGHKO', 'dronov@google.com', 'ACTIVE'),
       ('b3afa636-8006-42fe-961e-21ae926b3265', '44212253-a305-4495-9982-45e833aa74ac', 'Nikita', 'Smirnov', '$2a$10$m7IGC.7m1zoDLuQzIsz.O.OKxIeazoy8Sdrkk5Y1JXnqFWfineKhu', 'smirnov@google.com', 'ACTIVE'),
       ('3a472b53-236d-4cd9-a9d3-0d413ad3b903', '44212253-a305-4495-9982-45e833aa74ac', 'Igor', 'Shashkov', '$2a$10$W07EXiI4V40SRRXHZdRBcurorGTysiSsa5h03ZUJmblvctQ.UBW8W', 'shashkov@google.com', 'NOT_ACTIVE'),
       ('e1d0af96-cbe7-409d-bab7-e8ef64669362', '44212253-a305-4495-9982-45e833aa74ac', 'Oleg', 'Popov', '$2a$10$ch99apPuJoORMIf8Ew.D9e.cgWa1C6EYQ3iQMp7idTlGyNpyoF.P.', 'popov@google.com', 'NOT_ACTIVE');