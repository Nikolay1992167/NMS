INSERT INTO news (id, title, text, id_author)
VALUES ('7bb84799-46d7-4afe-80fa-90748469865a', 'SpaceX провела третий испытательный полёт Starship',
        '14 марта 2024 года SpaceX запустила в третий испытательный полёт корабль Starship Super Heavy. Корабль стартовал с площадки OLP-1 Starbase в Техасе. Цель миссии Integrated Flight Test 3 — выход корабля на расчётную траекторию орбиты.',
        '2512c298-6a1d-48d7-a12d-b51069aceb08');

INSERT INTO comments (id, created_by, text, username, news_id)
VALUES ('54bb52ec-685f-4c57-a538-56355797a037', '9439e220-7a43-4193-90d3-325ed3e5c8bd', 'Best company in America.', 'Nikolay', '7bb84799-46d7-4afe-80fa-90748469865a');