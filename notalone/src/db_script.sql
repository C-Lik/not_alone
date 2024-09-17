CREATE TABLE map
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL CHECK (length(name) <= 20)
);

INSERT INTO map(name)
VALUES ('map1'),
       ('map2'),
       ('map3');

CREATE TABLE current_map
(
    map_id INTEGER,
    won INTEGER,
    FOREIGN KEY (map_id) REFERENCES map (id)
);

CREATE TABLE item
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL CHECK (length(name) <= 20)
);


CREATE TABLE enemy
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL CHECK (length(name) <= 20)
);

INSERT INTO item(name)
VALUES ('potion'),
       ('portal');

INSERT INTO enemy(name)
VALUES ('hell_dog'),
       ('ghoul'),
       ('goblin');

CREATE TABLE maps_items
(
    map_id  INTEGER,
    item_id INTEGER,
    index_x INTEGER,
    index_y INTEGER,
    FOREIGN KEY (map_id) REFERENCES map (id),
    FOREIGN KEY (item_id) REFERENCES item(id)
);

CREATE TABLE maps_enemies
(
    map_id  INTEGER,
    enemy_id INTEGER,
    index_x INTEGER,
    index_y INTEGER,
    left_limit INTEGER,
    right_limit INTEGER,
    FOREIGN KEY (map_id) REFERENCES map (id),
    FOREIGN KEY (enemy_id) REFERENCES enemy(id)
);

/*
1 - potion
2 - portal
*/
INSERT INTO maps_items(map_id, item_id, index_x, index_y)
VALUES (1, 1, 49, 8),
       (1, 1, 98, 10),
       (1, 2, 121, 15),

       (2, 1, 54, 12),
       (2, 1, 106, 12),
       (2, 2, 111, 17),

       (3, 1, 58, 12),
       (3, 1, 20, 3),
       (3, 1, 100, 12),
       (3, 2, 122, 17);

/*
 1 - hell_dog
 2 - ghoul
 3 - goblin
 */
INSERT INTO maps_enemies(map_id, enemy_id, index_x, index_y, left_limit, right_limit)
VALUES (1,1, 35, 15, 29, 41),
       (1, 2, 72, 15, 59, 83),
       (1, 1, 99, 15, 85, 111),

       (2, 3, 34, 10, 26, 42),
       (2, 3, 56, 16, 44, 66),
       (2, 3, 82, 12, 72, 90),
       (2, 3, 111, 8, 94, 124),

       (3, 2, 58, 8, 46, 70),
       (3, 1, 42, 17, 42, 76),
       (3, 2, 79, 17,79, 96),
       (3, 1, 110, 8, 86, 111),
       (3, 2, 110, 17, 107, 124);
