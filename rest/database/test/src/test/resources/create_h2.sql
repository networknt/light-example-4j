DROP table IF EXISTS world;

CREATE TABLE  world (
  id int auto_increment primary key,
  randomNumber int NOT NULL
);


INSERT INTO world
SELECT id_seq.nextval,
dbms_random.value(1,10000)
FROM  dual
CONNECT BY level <= 10000;

