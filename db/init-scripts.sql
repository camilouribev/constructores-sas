create extension hstore;
create schema realestate;
create table if not exists realestate."Construction" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"construction_type" VARCHAR NOT NULL,
 "longitude" FLOAT NOT NULL,"latitude" FLOAT NOT NULL,"starting_date" DATE NOT NULL, "completion_date" DATE NOT NULL,
  "status" VARCHAR NOT NULL
);
create table if not exists realestate."Material" ("material_id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,
"available_quantity" INTEGER NOT NULL);
