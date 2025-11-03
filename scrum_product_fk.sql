START TRANSACTION;

-- 删除外键约束
ALTER TABLE `scrum_products` DROP FOREIGN KEY `fk_product_po`;
ALTER TABLE `scrum_products` DROP FOREIGN KEY `fk_product_sm`;
ALTER TABLE `scrum_products` DROP FOREIGN KEY `fk_product_team`;

-- 放宽字段为可为空
ALTER TABLE `scrum_products` MODIFY COLUMN `scrum_master_id` BIGINT NULL COMMENT 'Scrum Master id';
ALTER TABLE `scrum_products` MODIFY COLUMN `team_id` BIGINT NULL COMMENT '团队 id';

COMMIT;