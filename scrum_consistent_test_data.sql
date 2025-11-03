START TRANSACTION;

-- ================================
-- 0) 基础变量：产品/团队/迭代
-- ================================
-- 产品：SmartAdmin 平台（来自 scrum_product_insert.sql 中的 P-223C-0001）
SET @product_id := (SELECT id FROM `scrum_products` WHERE `product_code`='P-223C-0001' LIMIT 1);
-- 团队：优先取产品绑定团队，其次取 Team223A
SET @team_id := COALESCE(
  (SELECT `team_id` FROM `scrum_products` WHERE `id`=@product_id LIMIT 1),
  (SELECT id FROM `scrum_teams` WHERE `team_name`='Team223A' LIMIT 1)
);
-- 迭代：系统设置模块（来自 scrum_sprint_insert.sql）
SET @sprint_id := (
  SELECT id FROM `scrum_sprints`
  WHERE `product_id`=@product_id AND `sprint_name`='系统设置模块' LIMIT 1
);

-- ================================
-- 1) 插入验证用用户与团队成员
-- ================================
-- 开发人员用户（如已存在则跳过）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `email`, `is_disable`, `status`)
SELECT 'dev_a_223c', '$2a$10$9V8c.kEwJpF1qQmGdQ6d9e0pA3cYvCw6qJpUe3v1C9Q2nQF7x6o0e', '开发A', '17000000003', 'deva@example.com', 2, 1
WHERE NOT EXISTS (SELECT 1 FROM `sys_user` WHERE `username`='dev_a_223c');

INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `email`, `is_disable`, `status`)
SELECT 'dev_b_223c', '$2a$10$9V8c.kEwJpF1qQmGdQ6d9e0pA3cYvCw6qJpUe3v1C9Q2nQF7x6o0e', '开发B', '17000000004', 'devb@example.com', 2, 1
WHERE NOT EXISTS (SELECT 1 FROM `sys_user` WHERE `username`='dev_b_223c');

-- 取用户ID
SET @dev_a_user_id := (SELECT id FROM `sys_user` WHERE `username`='dev_a_223c');
SET @dev_b_user_id := (SELECT id FROM `sys_user` WHERE `username`='dev_b_223c');

-- 团队成员（如已存在则跳过）
INSERT INTO `scrum_team_members` (`team_id`, `user_id`, `member_role`, `post`, `name`, `start_time`, `hour4day`, `is_active`)
SELECT @team_id, @dev_a_user_id, 'DEV', '后端开发', '开发A', '2025-10-01', 8, 1
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_team_members` WHERE `team_id`=@team_id AND `user_id`=@dev_a_user_id
);

INSERT INTO `scrum_team_members` (`team_id`, `user_id`, `member_role`, `post`, `name`, `start_time`, `hour4day`, `is_active`)
SELECT @team_id, @dev_b_user_id, 'DEV', '前端开发', '开发B', '2025-10-01', 8, 1
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_team_members` WHERE `team_id`=@team_id AND `user_id`=@dev_b_user_id
);

-- 取团队成员ID
SET @dev_a_member_id := (SELECT id FROM `scrum_team_members` WHERE `team_id`=@team_id AND `user_id`=@dev_a_user_id LIMIT 1);
SET @dev_b_member_id := (SELECT id FROM `scrum_team_members` WHERE `team_id`=@team_id AND `user_id`=@dev_b_user_id LIMIT 1);

-- ================================
-- 2) 成员参与迭代（人日/周期一致性）
-- ================================
-- 将两位成员绑定到“系统设置模块”迭代
INSERT INTO `scrum_member_sprints` (`sprint_id`, `team_member_id`, `start_date`, `end_date`, `work_hours_per_day`)
SELECT @sprint_id, @dev_a_member_id, '2025-10-26', '2025-11-15', 8
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_member_sprints` WHERE `sprint_id`=@sprint_id AND `team_member_id`=@dev_a_member_id
);

INSERT INTO `scrum_member_sprints` (`sprint_id`, `team_member_id`, `start_date`, `end_date`, `work_hours_per_day`)
SELECT @sprint_id, @dev_b_member_id, '2025-10-26', '2025-11-15', 8
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_member_sprints` WHERE `sprint_id`=@sprint_id AND `team_member_id`=@dev_b_member_id
);

-- ================================
-- 3) 插入 Sprint 待办项（与前端示例图对应）
-- ================================
-- 待办1：登录界面优化（开发A）
INSERT INTO `scrum_sprint_backlogs` (`sprint_id`, `team_member_id`, `backlog_title`, `backlog_status`, `estimated_hours`)
SELECT @sprint_id, @dev_a_member_id, '登录界面优化', '2', 16.0
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprint_backlogs` WHERE `sprint_id`=@sprint_id AND `backlog_title`='登录界面优化'
);

-- 待办2：权限模块重构（开发A）
INSERT INTO `scrum_sprint_backlogs` (`sprint_id`, `team_member_id`, `backlog_title`, `backlog_status`, `estimated_hours`)
SELECT @sprint_id, @dev_a_member_id, '权限模块重构', '2', 48.0
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprint_backlogs` WHERE `sprint_id`=@sprint_id AND `backlog_title`='权限模块重构'
);

-- 待办3：菜单管理实现（开发B）
INSERT INTO `scrum_sprint_backlogs` (`sprint_id`, `team_member_id`, `backlog_title`, `backlog_status`, `estimated_hours`)
SELECT @sprint_id, @dev_b_member_id, '菜单管理实现', '2', 40.0
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprint_backlogs` WHERE `sprint_id`=@sprint_id AND `backlog_title`='菜单管理实现'
);

-- 待办4：系统配置接口（开发B）
INSERT INTO `scrum_sprint_backlogs` (`sprint_id`, `team_member_id`, `backlog_title`, `backlog_status`, `estimated_hours`)
SELECT @sprint_id, @dev_b_member_id, '系统配置接口', '2', 24.0
WHERE NOT EXISTS (
  SELECT 1 FROM `scrum_sprint_backlogs` WHERE `sprint_id`=@sprint_id AND `backlog_title`='系统配置接口'
);

-- 取待办ID
SET @bl_login   := (SELECT id FROM `scrum_sprint_backlogs` WHERE `sprint_id`=@sprint_id AND `backlog_title`='登录界面优化' LIMIT 1);
SET @bl_auth    := (SELECT id FROM `scrum_sprint_backlogs` WHERE `sprint_id`=@sprint_id AND `backlog_title`='权限模块重构' LIMIT 1);
SET @bl_menu    := (SELECT id FROM `scrum_sprint_backlogs` WHERE `sprint_id`=@sprint_id AND `backlog_title`='菜单管理实现' LIMIT 1);
SET @bl_config  := (SELECT id FROM `scrum_sprint_backlogs` WHERE `sprint_id`=@sprint_id AND `backlog_title`='系统配置接口' LIMIT 1);

-- ================================
-- 4) 插入开发记录（日志数/实际工时通过聚合）
--    目标：该迭代总实际工时 = 168 小时（与迭代 consumed_hours 保持一致）
-- ================================
-- 登录界面优化：17小时（2条）
INSERT INTO `scrum_development_records` (`product_id`, `sprint_id`, `sprint_backlog_id`, `team_member_id`, `work_date`, `completed_hours`, `actual_hours`, `development_progress`, `planned_hours`, `daily_summary`)
VALUES
(@product_id, @sprint_id, @bl_login,  @dev_a_member_id, '2025-10-27 10:00:00', 8.0, 8.0, 0.20, 8, 'UI结构调整'),
(@product_id, @sprint_id, @bl_login,  @dev_a_member_id, '2025-10-28 10:00:00', 9.0, 9.0, 0.45, 8, '交互与校验优化');

-- 权限模块重构：60小时（4条，每次15小时）
INSERT INTO `scrum_development_records` (`product_id`, `sprint_id`, `sprint_backlog_id`, `team_member_id`, `work_date`, `completed_hours`, `actual_hours`, `development_progress`, `planned_hours`, `daily_summary`)
VALUES
(@product_id, @sprint_id, @bl_auth,   @dev_a_member_id, '2025-10-29 10:00:00', 15.0, 15.0, 0.25, 8, '角色模型梳理'),
(@product_id, @sprint_id, @bl_auth,   @dev_a_member_id, '2025-10-30 10:00:00', 15.0, 15.0, 0.50, 8, '权限边界与审计'),
(@product_id, @sprint_id, @bl_auth,   @dev_a_member_id, '2025-10-31 10:00:00', 15.0, 15.0, 0.75, 8, '接口与策略'),
(@product_id, @sprint_id, @bl_auth,   @dev_a_member_id, '2025-11-01 10:00:00', 15.0, 15.0, 0.95, 8, '联调与回归');

-- 菜单管理实现：50小时（3条：17+16+17）
INSERT INTO `scrum_development_records` (`product_id`, `sprint_id`, `sprint_backlog_id`, `team_member_id`, `work_date`, `completed_hours`, `actual_hours`, `development_progress`, `planned_hours`, `daily_summary`)
VALUES
(@product_id, @sprint_id, @bl_menu,   @dev_b_member_id, '2025-11-02 10:00:00', 17.0, 17.0, 0.40, 8, '菜单数据结构'),
(@product_id, @sprint_id, @bl_menu,   @dev_b_member_id, '2025-11-03 10:00:00', 16.0, 16.0, 0.70, 8, '路由与权限绑定'),
(@product_id, @sprint_id, @bl_menu,   @dev_b_member_id, '2025-11-04 10:00:00', 17.0, 17.0, 0.95, 8, '界面与测试');

-- 系统配置接口：41小时（3条：12+8+21）
INSERT INTO `scrum_development_records` (`product_id`, `sprint_id`, `sprint_backlog_id`, `team_member_id`, `work_date`, `completed_hours`, `actual_hours`, `development_progress`, `planned_hours`, `daily_summary`)
VALUES
(@product_id, @sprint_id, @bl_config, @dev_b_member_id, '2025-11-05 10:00:00', 12.0, 12.0, 0.35, 8, '接口设计'),
(@product_id, @sprint_id, @bl_config, @dev_b_member_id, '2025-11-06 10:00:00', 8.0, 8.0, 0.50, 8, '参数与校验'),
(@product_id, @sprint_id, @bl_config, @dev_b_member_id, '2025-11-07 10:00:00', 21.0, 21.0, 0.95, 8, '联调与文档');

-- ================================
-- 5) 同步迭代的已消耗工时（consumed_hours），确保与开发记录聚合一致
-- ================================
SET @sum_actual := (
  SELECT COALESCE(SUM(actual_hours),0) FROM `scrum_development_records` WHERE `sprint_id`=@sprint_id
);
UPDATE `scrum_sprints` SET `consumed_hours` = @sum_actual WHERE `id`=@sprint_id;

-- 可选：将部分待办状态标记为完成
UPDATE `scrum_sprint_backlogs` SET `backlog_status`='3' WHERE `id` IN (@bl_login, @bl_auth);

COMMIT;

-- ================================
-- 6) 验证查询（执行后可用）
-- ================================
-- 迭代总实际工时应为 168
-- SELECT SUM(actual_hours) FROM `scrum_development_records` WHERE `sprint_id`=@sprint_id;
-- 迭代 consumed_hours 字段应等于上述结果
-- SELECT `consumed_hours` FROM `scrum_sprints` WHERE `id`=@sprint_id;
-- 待办实际工时（Mapper将按开发记录聚合显示）
-- SELECT `backlog_title`, (SELECT SUM(dr.actual_hours) FROM scrum_development_records dr WHERE dr.sprint_backlog_id=sb.id) AS actual_sum
-- FROM `scrum_sprint_backlogs` sb WHERE sb.sprint_id=@sprint_id;