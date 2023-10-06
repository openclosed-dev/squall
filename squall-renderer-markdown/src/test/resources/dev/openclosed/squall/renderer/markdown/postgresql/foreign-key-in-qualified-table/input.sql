CREATE DATABASE db1;

CREATE SCHEMA schema1;

CREATE TABLE schema1.products (
  product_no integer PRIMARY KEY
);

CREATE TABLE schema1.orders (
  order_id integer PRIMARY KEY,
  product_no integer REFERENCES schema1.products (product_no)
);
