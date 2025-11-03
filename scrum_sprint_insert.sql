

-- 为安全起见，先关闭外键检查（便于执行变更与插入），完成后再开启
SET FOREIGN_KEY_CHECKS = 0;

-- ========== 0. 获取产品ID ==========
SET @product_smartadmin_id := (SELECT id FROM `scrum_products` WHERE `product_code` = 'P-223C-0001' LIMIT 1);
SET @product_datacenter_id := (SELECT id FROM `scrum_products` WHERE `product_code` = 'P-223C-0002' LIMIT 1);

-- ========== 1. 永久去掉 scrum_sprints 的外键约束 ==========
-- 约束名称来自建表语句：fk_sprint_product, fk_sprint_team
-- 如不存在则忽略（使用动态 SQL 检测并删除）

-- 删除 fk_sprint_product
SET @fk_name := (SELECT CONSTRAINT_NAME FROM information_schema.TABLE_CONSTRAINTS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scrum_sprints' AND CONSTRAINT_TYPE = 'FOREIGN KEY' AND CONSTRAINT_NAME = 'fk_sprint_product' LIMIT 1);
SET @sql := IF(@fk_name IS NULL, 'SELECT 1', CONCAT('ALTER TABLE `scrum_sprints` DROP FOREIGN KEY `', @fk_name, '`'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 删除 fk_sprint_team
SET @fk_name := (SELECT CONSTRAINT_NAME FROM information_schema.TABLE_CONSTRAINTS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scrum_sprints' AND CONSTRAINT_TYPE = 'FOREIGN KEY' AND CONSTRAINT_NAME = 'fk_sprint_team' LIMIT 1);
SET @sql := IF(@fk_name IS NULL, 'SELECT 1', CONCAT('ALTER TABLE `scrum_sprints` DROP FOREIGN KEY `', @fk_name, '`'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 允许未分配团队（便于前端“显示未分配团队”筛选）
ALTER TABLE `scrum_sprints` MODIFY COLUMN `team_id` BIGINT NULL COMMENT '负责团队ID（关联scrum_teams.id，可为空）';

-- ========== 2. 更新产品（SmartAdmin 平台）项目级信息 ==========
-- 设置总人日与项目周期（确保严格大于任一迭代的人日与周期）
UPDATE `scrum_products`
SET `total_man_day` = 150.00,
    `plan_start_date` = '2025-10-01',
    `plan_end_date`   = '2025-12-01'
WHERE `id` = @product_smartadmin_id;

-- 数据中台产品已有：total_man_day = 200，周期 2025-11-01 ~ 2026-03-31（无需变更）

-- ========== 3. 插入团队数据（若不存在则插入） ==========
INSERT INTO `scrum_teams` (`team_name`, `header_id`, `team_description`, `notes`, `team_status`, `is_deleted`)
SELECT 'Team223A', NULL, 'SmartAdmin 平台团队', '基础架构与权限模块', 1, 0
WHERE NOT EXISTS (SELECT 1 FROM `scrum_teams` WHERE `team_name` = 'Team223A');

INSERT INTO `scrum_teams` (`team_name`, `header_id`, `team_description`, `notes`, `team_status`, `is_deleted`)
SELECT 'Team223B', NULL, '数据中台团队', '数据采集与治理', 1, 0
WHERE NOT EXISTS (SELECT 1 FROM `scrum_teams` WHERE `team_name` = 'Team223B');

SET @team_223a_id := (SELECT id FROM `scrum_teams` WHERE `team_name` = 'Team223A' LIMIT 1);
SET @team_223b_id := (SELECT id FROM `scrum_teams` WHERE `team_name` = 'Team223B' LIMIT 1);

-- ========== 4. 插入迭代数据 ==========
-- 4.1 SmartAdmin 平台（项目级：150人日，2025-10-01 ~ 2025-12-01）
INSERT INTO `scrum_sprints` (`sprint_name`, `product_id`, `team_id`, `sprint_goal`, `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`, `start_date`, `end_date`, `remark`)
SELECT '基础架构搭建', @product_smartadmin_id, @team_223a_id, '完成平台基础框架与脚手架', 'completed', 100, 25.0, 200, '2025-10-01', '2025-10-10', '第一个迭代：框架与基础设施'
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprints` WHERE `product_id` = @product_smartadmin_id AND `sprint_name` = '基础架构搭建'
);

INSERT INTO `scrum_sprints` (`sprint_name`, `product_id`, `team_id`, `sprint_goal`, `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`, `start_date`, `end_date`, `remark`)
SELECT '用户管理模块', @product_smartadmin_id, @team_223a_id, '实现用户注册登录与权限体系', 'completed', 100, 30.0, 240, '2025-10-11', '2025-10-25', 'RBAC/审计完成'
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprints` WHERE `product_id` = @product_smartadmin_id AND `sprint_name` = '用户管理模块'
);

INSERT INTO `scrum_sprints` (`sprint_name`, `product_id`, `team_id`, `sprint_goal`, `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`, `start_date`, `end_date`, `remark`)
SELECT '系统设置模块', @product_smartadmin_id, @team_223a_id, '系统配置、菜单与组织结构', 'in_progress', 60, 35.0, 168, '2025-10-26', '2025-11-15', '设置模块进行中'
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprints` WHERE `product_id` = @product_smartadmin_id AND `sprint_name` = '系统设置模块'
);

INSERT INTO `scrum_sprints` (`sprint_name`, `product_id`, `team_id`, `sprint_goal`, `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`, `start_date`, `end_date`, `remark`)
SELECT '报表统计模块', @product_smartadmin_id, @team_223a_id, '统计报表与导出能力', 'not_started', 0, 25.0, 0, '2025-11-16', '2025-11-30', '待开发'
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprints` WHERE `product_id` = @product_smartadmin_id AND `sprint_name` = '报表统计模块'
);

-- 4.2 数据中台（项目级：200人日，2025-11-01 ~ 2026-03-31）
INSERT INTO `scrum_sprints` (`sprint_name`, `product_id`, `team_id`, `sprint_goal`, `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`, `start_date`, `end_date`, `remark`)
SELECT '需求分析与架构设计', @product_datacenter_id, @team_223b_id, '需求调研、技术选型与总体架构', 'completed', 100, 40.0, 320, '2025-11-01', '2025-11-20', '架构定版'
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprints` WHERE `product_id` = @product_datacenter_id AND `sprint_name` = '需求分析与架构设计'
);

INSERT INTO `scrum_sprints` (`sprint_name`, `product_id`, `team_id`, `sprint_goal`, `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`, `start_date`, `end_date`, `remark`)
SELECT '数据采集层开发', @product_datacenter_id, @team_223b_id, '多源采集、清洗与标准化', 'in_progress', 60, 45.0, 216, '2025-11-21', '2025-12-15', '采集层进行中'
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprints` WHERE `product_id` = @product_datacenter_id AND `sprint_name` = '数据采集层开发'
);

INSERT INTO `scrum_sprints` (`sprint_name`, `product_id`, `team_id`, `sprint_goal`, `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`, `start_date`, `end_date`, `remark`)
SELECT '数据存储与治理', @product_datacenter_id, @team_223b_id, '仓库搭建、血缘追踪与质量规则', 'not_started', 0, 50.0, 0, '2025-12-16', '2026-01-20', '待开发'
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprints` WHERE `product_id` = @product_datacenter_id AND `sprint_name` = '数据存储与治理'
);

INSERT INTO `scrum_sprints` (`sprint_name`, `product_id`, `team_id`, `sprint_goal`, `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`, `start_date`, `end_date`, `remark`)
SELECT '数据服务与API', @product_datacenter_id, @team_223b_id, '数据查询接口与API网关', 'not_started', 0, 35.0, 0, '2026-01-21', '2026-02-15', '待开发'
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprints` WHERE `product_id` = @product_datacenter_id AND `sprint_name` = '数据服务与API'
);

INSERT INTO `scrum_sprints` (`sprint_name`, `product_id`, `team_id`, `sprint_goal`, `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`, `start_date`, `end_date`, `remark`)
SELECT '可视化与监控', @product_datacenter_id, @team_223b_id, '数据大屏与系统监控告警', 'not_started', 0, 30.0, 0, '2026-02-16', '2026-03-15', '待开发'
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprints` WHERE `product_id` = @product_datacenter_id AND `sprint_name` = '可视化与监控'
);

-- ========== 5. 恢复外键检查 ==========
SET FOREIGN_KEY_CHECKS = 1;

-- ========== 6. 校验（可选） 