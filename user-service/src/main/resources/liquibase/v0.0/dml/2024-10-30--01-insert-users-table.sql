--liquibase formatted sql
--changeset Minich:1
INSERT INTO users (id, first_name, last_name, password, email, status)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Ivan', 'Sidorov', '$2a$10$NzKyyNVQ66G/.kLg/HTtSe1.9aqJWtKxTZTXp9LqkP3SZUzDzQLHG', 'ivan1@google.com', 'ACTIVE'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Egor', 'Strelin', '$2a$10$uuwsQHbWZMIUMTuxKwij8e/l5zea9.Q2XW0eG3Bs/2fUMarbqiymG', 'strelin@mail.ru', 'ACTIVE'),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Alex', 'Volk', '$2a$10$wH5b5g3QibOOdDhOVlSGxuyvqOO4kDcWMI3TQKNK9HdzUeQLowmNG', 'volk@google.com', 'ACTIVE');