INSERT INTO news (id, title, text, id_author)
VALUES ('acd9356c-07b9-422c-9140-95fd8d111bf6', 'SpaceX updates settlement plans on Mars', 'SpaceX has announced new priorities for Starship that will pave the way for full and rapid reuse, which is a key factor for missions to Mars.', 'de1128e7-715c-448d-a068-d8273fa4c57d'),
       ('166f30de-5356-4385-8d0d-eb1d37b07689', 'Xiaomi releases the SU7 electric car', 'Xiaomi has said it can produce its popular full-size SU7 sedan in just 76 seconds, which has sparked interest in the automotive industry.', 'de1128e7-715c-448d-a068-d8273fa4c57d'),
       ('e551e6c4-e1df-4084-8872-51c76e0b4801', 'Mercedes-Benz E450 sets new standards for comfort', 'With a new six-cylinder engine and luxury in every detail, the Mercedes-Benz E450 raises the bar for premium sedans.', 'b3afa636-8006-42fe-961e-21ae926b3265');

INSERT INTO comments (id, text, username, news_id)
VALUES ('98484a32-71c6-44cf-8686-ca8f528d6ad5', 'The first in world.', 'Nikolay', 'acd9356c-07b9-422c-9140-95fd8d111bf6'),
       ('8cc4b223-d135-4a4e-b0c3-dc914036c539', 'Very-very good news.', 'Igor', 'acd9356c-07b9-422c-9140-95fd8d111bf6'),
       ('b3846354-f0a7-442d-b5d1-7aab13d69165', 'We will definitely explore space.', 'Oleg', 'acd9356c-07b9-422c-9140-95fd8d111bf6'),
       ('fab7c5d6-8fe4-4d03-8390-18bcbd81cf8c', 'It is a good car.', 'Yury', '166f30de-5356-4385-8d0d-eb1d37b07689'),
       ('df289596-c2e0-4064-8625-ae15015ab9fa', 'The right direction.', 'Pavel', '166f30de-5356-4385-8d0d-eb1d37b07689'),
       ('ff14480e-8c48-4e3e-b759-abae20063b8e', 'I would like such a car Xiaomi.', 'Sveta', '166f30de-5356-4385-8d0d-eb1d37b07689'),
       ('df825651-c7d0-49bb-b00d-e906e9144d30', 'Mercedes is the ideal of comfort.', 'Egor', 'e551e6c4-e1df-4084-8872-51c76e0b4801'),
       ('9b0f0069-a187-43e7-8465-69f7471820d3', 'The best cars are German as always.', 'Helena', 'e551e6c4-e1df-4084-8872-51c76e0b4801'),
       ('e70bb8df-b80a-4f3b-b002-a289d91f752f', 'Probably this car will cost a lot of money.', 'Dmitriy', 'e551e6c4-e1df-4084-8872-51c76e0b4801');