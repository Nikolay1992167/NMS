--liquibase formatted sql
--changeset Minich:2
INSERT INTO comments (id, text, username, news_id)
VALUES ('09434a73-e4e5-463b-a772-f282adef33a9', 'Best company in America.', 'Nikolay', '6a268b16-85ea-491f-be9f-f2e28d1a4895'),
       ('0af362c7-999e-428d-91ec-703ee540c54a', 'Good news.', 'Igor', '6a268b16-85ea-491f-be9f-f2e28d1a4895'),
       ('4d0eb5fb-6c3b-4512-9c75-7a8ce5eac4e9', 'We will definitely explore space.', 'Oleg', '6a268b16-85ea-491f-be9f-f2e28d1a4895'),
       ('5a0d7e7c-bbac-4165-a4bd-cc9ca0b05ef7', 'It is a good car.', 'Yury', '389ec033-0631-4c0a-825b-e9ed165104d7'),
       ('f1881042-e35c-4d99-858f-2585954240ce', 'The right direction.', 'Pavel', '389ec033-0631-4c0a-825b-e9ed165104d7'),
       ('f36685fa-00b3-4b80-a064-97599fba9d5c', 'I would like such a car Xiaomi.', 'Sveta', '389ec033-0631-4c0a-825b-e9ed165104d7'),
       ('190763b6-6e0c-44fd-b770-cb6283e4e272', 'Mercedes is the ideal of comfort.', 'Egor', '0a30edeb-a294-43bc-a8b8-e5de7e2c8e08'),
       ('72e53e9c-2f06-4cda-88a2-9d926115813b', 'The best cars are German as always.', 'Helena', '0a30edeb-a294-43bc-a8b8-e5de7e2c8e08'),
       ('8670b4d6-8c92-4119-882d-530ea13cf3a0', 'Probably this car will cost a lot of money.', 'Dmitriy', '0a30edeb-a294-43bc-a8b8-e5de7e2c8e08'),
       ('1df46147-6686-4355-9fa2-43be5d712caa', 'I don''t see the charms, the car is like a car.', 'Leonid', 'f0759f37-00ec-456f-a555-e634dc90a1f6'),
       ('d3e58657-c240-48bd-afb8-816592a22c67', 'It looks good, but I''m not a connoisseur, I don''t care.', 'Radion', 'f0759f37-00ec-456f-a555-e634dc90a1f6'),
       ('0c0f2475-18de-45c7-ba3d-3db78b908c43', 'Toyota has beautiful cars.', 'Marina', 'f0759f37-00ec-456f-a555-e634dc90a1f6'),
       ('c8d8bc8c-f869-4f45-a4e4-b813cd7f342d', 'I didn''t see a problem with that.', 'Slava', 'd215d55e-fe9b-4946-b55f-6b428cf7a68b'),
       ('bbeeeb25-0a04-4309-a071-e68172b27f44', 'Audi is doing great as it is.', 'Sergey', 'd215d55e-fe9b-4946-b55f-6b428cf7a68b'),
       ('443039da-b0cc-43d9-af77-c35433e03a11', 'They must have come up with something terrible.', 'Fedor', 'd215d55e-fe9b-4946-b55f-6b428cf7a68b');
