create table `user`
(
    `id` char(36) not null,
    `name` varchar(255) not null,
    `jira_api_key` text not null
);

create table `category`
(
    `user_id` char(36) not null,
    `outlook_category_id` char(36),
    `jira_project_id` varchar(36) not null,
    `default` bool,
    constraint category_udx unique (user_id, outlook_category_id)
);

create table `ignored_category`
(
    `id` char(36) not null,
    `user_id` char(36) not null,
    `name` varchar(255) not null
);
