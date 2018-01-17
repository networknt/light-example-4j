CREATE SCHEMA IF NOT EXISTS customerorder;
SET SCHEMA customerorder;

DROP table IF EXISTS  customer;


CREATE  TABLE customer (
  customer_id long,
  name varchar(255),
  creditLimit numeric
);
