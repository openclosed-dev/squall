CREATE DATABASE order_db;

CREATE TABLE customer (
  id varchar(64) PRIMARY KEY,
  name varchar(256) REQUIRED,
  age integer
);
