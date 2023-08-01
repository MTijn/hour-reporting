ALTER TABLE `hour_reporting`.`ignored_category`
    ADD COLUMN `outlook_category_id` CHAR(36) NOT NULL AFTER `user_id`;
