DROP Table IF Exists message;
DROP Table IF Exists received_messages;
DROP Table IF Exists saga_instance;
DROP Table IF Exists saga_instance_participants;
DROP Table IF Exists aggre_instance_subscription;
DROP Table IF Exists saga_lock_table;
DROP Table IF Exists saga_stash_table;
DROP Table IF Exists saga_enlisted_aggregates;

CREATE TABLE message (
  ID VARCHAR(120) PRIMARY KEY,
  DESTINATION VARCHAR(1000) NOT NULL,
  HEADERS VARCHAR(1000) NOT NULL,
  PAYLOAD VARCHAR(1000) NOT NULL
);

CREATE TABLE received_messages (
  CONSUMER_ID VARCHAR(120),
  MESSAGE_ID VARCHAR(120),
  PRIMARY KEY(CONSUMER_ID, MESSAGE_ID)
);

CREATE TABLE saga_instance(
  saga_type VARCHAR(100) NOT NULL,
  saga_id VARCHAR(100) NOT NULL,
  state_name VARCHAR(100) NOT NULL,
  last_request_id VARCHAR(100),
  saga_data_type VARCHAR(1000) NOT NULL,
  saga_data_json VARCHAR(1000) NOT NULL,
  PRIMARY KEY(saga_type, saga_id)
);


CREATE TABLE saga_instance_participants (
  saga_type VARCHAR(100) NOT NULL,
  saga_id VARCHAR(100) NOT NULL,
  destination VARCHAR(100) NOT NULL,
  resource VARCHAR(100) NOT NULL,
  PRIMARY KEY(saga_type, saga_id, destination, resource)
);

CREATE TABLE aggre_instance_subscription(
  aggregate_type VARCHAR(100) DEFAULT NULL,
  aggregate_id VARCHAR(100) NOT NULL,
  event_type VARCHAR(100) NOT NULL,
  saga_id VARCHAR(100) NOT NULL,
  saga_type VARCHAR(100) NOT NULL,
  PRIMARY KEY(aggregate_id, event_type, saga_id, saga_type)
);

create table saga_lock_table(
  target VARCHAR(100) NOT NULL,
  saga_type VARCHAR(100) NOT NULL,
  saga_Id VARCHAR(100) NOT NULL,
  PRIMARY KEY(target)
);

create table saga_stash_table(
  message_id VARCHAR(100) PRIMARY KEY,
  target VARCHAR(100) NOT NULL,
  saga_type VARCHAR(100) NOT NULL,
  saga_id VARCHAR(100) NOT NULL,
  message_headers VARCHAR(1000) NOT NULL,
  message_payload VARCHAR(1000) NOT NULL
  );

create table saga_enlisted_aggregates(
  saga_id VARCHAR(100) NOT NULL,
  aggregate_id VARCHAR(100) NOT NULL,
  aggregate_type VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY(aggregate_id,  saga_id)
  );
