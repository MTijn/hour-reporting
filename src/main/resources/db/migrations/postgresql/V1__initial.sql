create table "user"
(
    "id" uuid not null,
    "name" varchar not null,
    "clockify_api_key" varchar not null
);

create table "category"
(
    "id" uuid not null,
    "user_id" uuid not null,
    "name" varchar not null,
    "clockify_project_id" varchar not null,
    "default" boolean
);

create table "ignored_category"
(
    "id" uuid not null,
    "user_id" uuid not null,
    "name" varchar not null
);

create unique index "category_id_uindex"
    on category (id);

create unique index "ignored_category_id_uindex"
    on ignored_category (id);
