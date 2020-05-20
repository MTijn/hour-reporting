create schema if not exists hour_reporting;

create table hour_reporting."user"
(
    "id" uuid not null,
    "name" varchar not null,
    "clockify_api_key" varchar not null
);

create table category
(
    "id" uuid not null,
    "user_id" uuid not null,
    "name" varchar not null,
    "clockify_project_id" varchar not null,
    "default" boolean
);

create unique index category_id_uindex
    on category (id);
