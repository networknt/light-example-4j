
DROP table IF EXISTS  account_info;
DROP table IF EXISTS  account_transaction;
DROP table IF EXISTS  customer;
DROP table IF EXISTS  account_customer;
DROP table IF EXISTS  address;
DROP table IF EXISTS  account_change_info;


CREATE  TABLE account_info (
  account_id varchar(255),
  title varchar(255),
  description varchar(255),
  version varchar(255),
  balance numeric,
  creation_Date date,
  active_flg varchar(1) DEFAULT 'Y',
  PRIMARY KEY(account_id)
);


CREATE  TABLE account_transaction (
  transaction_Id varchar(255),
  from_account_id varchar(255),
  to_account_id varchar(255),
  amount numeric,
  creation_Date date,
  description varchar(255),
  status varchar(20),
  entry_Type varchar(20),
  active_flg varchar(1) DEFAULT 'Y',
  PRIMARY KEY(transaction_Id)
);

CREATE  TABLE account_change_info (
  change_id varchar(255),
  transaction_id varchar(255),
  transaction_type varchar(20),
  version varchar(255),
  amount numeric,
  balanceDelta numeric,
  creation_Date date,
  PRIMARY KEY(change_id)
);


CREATE  TABLE customer (
  customer_Id varchar(255),
  first_name varchar(60),
  last_name varchar(60),
  password varchar(255),
  ssn varchar(20),
  email varchar(60),
  phoneNumber varchar(20),
  active_flg varchar(1) DEFAULT 'Y',
  PRIMARY KEY(customer_Id)
);


CREATE  TABLE account_customer (
  account_id varchar(255),
  customer_Id varchar(255),
  PRIMARY KEY(account_id,customer_Id)
);


CREATE  TABLE address (
  customer_Id varchar(255),
  street1 varchar(100),
  street2 varchar(100),
  city varchar(40),
  state varchar(40),
  country varchar(40),
  zipCode varchar(20),
  PRIMARY KEY(customer_Id)
);
