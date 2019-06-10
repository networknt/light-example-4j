DROP TABLE world CASCADE CONSTRAINTS;
CREATE TABLE  world (
  id int NOT NULL,
  randomNumber int NOT NULL,
  PRIMARY KEY  (id)
);

DROP SEQUENCE id_seq;
CREATE SEQUENCE id_seq START WITH 1;

INSERT INTO world
SELECT id_seq.nextval,
dbms_random.value(1,10)
FROM  dual
CONNECT BY level <= 10;

DROP TABLE fortune CASCADE CONSTRAINTS;
CREATE TABLE fortune (
  id int NOT NULL,
  message varchar2(2048) NOT NULL,
  PRIMARY KEY  (id)
);

INSERT INTO fortune (id, message) VALUES (1, 'fortune: No such file or directory');
INSERT INTO fortune (id, message) VALUES (2, 'A computer scientist is someone who fixes things that aren''t broken.');
INSERT INTO fortune (id, message) VALUES (3, 'After enough decimal places, nobody gives a damn.');
INSERT INTO fortune (id, message) VALUES (4, 'A bad random number generator: 1, 1, 1, 1, 1, 4.33e+67, 1, 1, 1');
INSERT INTO fortune (id, message) VALUES (5, 'A computer program does what you tell it to do, not what you want it to do.');
INSERT INTO fortune (id, message) VALUES (6, 'Emacs is a nice operating system, but I prefer UNIX. — Tom Christaensen');
INSERT INTO fortune (id, message) VALUES (7, 'Any program that runs right is obsolete.');
INSERT INTO fortune (id, message) VALUES (8, 'A list is only as strong as its weakest link. — Donald Knuth');
INSERT INTO fortune (id, message) VALUES (9, 'Feature: A bug with seniority.');
INSERT INTO fortune (id, message) VALUES (10, 'Computers make very fast, very accurate mistakes.');
INSERT INTO fortune (id, message) VALUES (11, '<script>alert("This should not be displayed in a browser alert box.");</script>');
INSERT INTO fortune (id, message) VALUES (12, 'フレームワークのベンチマーク');
