ALTER TABLE `hour_reporting`.`user`
    ADD COLUMN `jira_user_name` VARCHAR(255) NOT NULL AFTER `jira_api_key`;
