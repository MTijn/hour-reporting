ALTER TABLE `hour_reporting`.`ignored_category`
    DROP COLUMN `id`,
    DROP COLUMN `outlook_category_id`,
    ADD UNIQUE INDEX `user_category_udx` (`user_id` ASC, `name` ASC);
;
