CREATE TABLE category
(
    user_id         uuid         NOT NULL,
    category_name   varchar(255) NOT NULL,
    jira_project_id uuid         NOT NULL,
    jira_issue_key  varchar(255) NOT NULL,
    "default"       bool DEFAULT NULL,
    CONSTRAINT category_udx UNIQUE (user_id, category_name)
);

CREATE TABLE ignored_category
(
    user_id uuid         NOT NULL,
    name    varchar(255) NOT NULL,
    CONSTRAINT user_category_udx UNIQUE (user_id, name)
);

CREATE TABLE "user"
(
    id   uuid NOT NULL,
    data json DEFAULT NULL,
    PRIMARY KEY (id)
);
-- Dump completed on 2025-02-03 14:28:01
