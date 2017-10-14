CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;

CREATE TABLE FUser(
  username citext NOT NULL PRIMARY KEY,
  email citext NOT NULL UNIQUE,
  password citext,
  score int8 DEFAULT 0
);