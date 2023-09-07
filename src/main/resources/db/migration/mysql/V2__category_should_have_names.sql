TRUNCATE TABLE `category`;

ALTER TABLE `hour_reporting`.`category`
    CHANGE COLUMN `outlook_category_id` `category_name` VARCHAR(255) NOT NULL;
