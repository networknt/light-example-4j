DROP TABLE IF EXISTS t;

create table t (
  msg varchar PRIMARY KEY
);

INSERT INTO t(msg) VALUES('msg100 from db1');
INSERT INTO t(msg) VALUES('msg200 from db1');
