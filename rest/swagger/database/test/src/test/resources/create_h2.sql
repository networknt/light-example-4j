DROP table IF EXISTS world;

CREATE TABLE  world (
  id int auto_increment primary key,
  randomNumber int NOT NULL
);


INSERT INTO world(id, randomNumber) VALUES (1, 10);
INSERT INTO world(id, randomNumber) VALUES (2, 9);
INSERT INTO world(id, randomNumber) VALUES (3, 8);
INSERT INTO world(id, randomNumber) VALUES (4, 7);
INSERT INTO world(id, randomNumber) VALUES (5, 6);
INSERT INTO world(id, randomNumber) VALUES (6, 5);
INSERT INTO world(id, randomNumber) VALUES (7, 4);
INSERT INTO world(id, randomNumber) VALUES (8, 3);
INSERT INTO world(id, randomNumber) VALUES (9, 2);
INSERT INTO world(id, randomNumber) VALUES (10, 1);
