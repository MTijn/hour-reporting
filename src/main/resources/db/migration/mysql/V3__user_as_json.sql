TRUNCATE TABLE `user`;

ALTER TABLE `user`
    DROP COLUMN `name`,
    DROP COLUMN `jira_api_key`,
    DROP COLUMN `jira_user_name`,
    DROP COLUMN `photo`;

ALTER TABLE `user`
    ADD COLUMN `data` JSON,
    ADD PRIMARY KEY(`id`);

TRUNCATE TABLE `category`;

ALTER TABLE `category`
    ADD COLUMN `jira_issue_key` BIGINT AFTER jira_project_id;
