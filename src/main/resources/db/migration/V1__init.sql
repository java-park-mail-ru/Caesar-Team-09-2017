CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;

CREATE TABLE FUser(
  id SERIAL8 PRIMARY KEY,
  username citext NOT NULL UNIQUE,
  email citext UNIQUE,
  password citext,
  score int8 DEFAULT 0
);