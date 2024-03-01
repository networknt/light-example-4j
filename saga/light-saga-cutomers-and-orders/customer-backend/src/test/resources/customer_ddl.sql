CREATE SCHEMA IF NOT EXISTS customerorder;
SET SCHEMA customerorder;

DROP table IF EXISTS  customer;
DROP table IF EXISTS  customer_order;


CREATE  TABLE customer (
  customer_id long,
  name varchar(255),
  creditLimit numeric
);

CREATE  TABLE customer_order (
  customer_id long,
  order_id long,
  orderTotal numeric
);
