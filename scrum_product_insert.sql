START TRANSACTION;

-- 1) 插入用户（PO、SM）
-- 密码放入BCrypt示例哈希；仅用于满足非空要求，不用于登录验证
INSERT INTO `sys_user` (
  `username`, `password`, `real_name`, `phone`, `email`, `is_disable`, `status`
) VALUES
('po_user_223c', '$2a$10$9V8c.kEwJpF1qQmGdQ6d9e0pA3cYvCw6qJpUe3v1C9Q2nQF7x6o0e', '产品负责人', '17000000001', 'po@example.com', 2, 1),
('sm_user_223c', '$2a$10$9V8c.kEwJpF1qQmGdQ6d9e0pA3cYvCw6qJpUe3v1C9Q2nQF7x6o0e', 'Scrum Master', '17000000002', 'sm@example.com', 2, 1);

-- 取回用户ID
SET @po_id := (SELECT id FROM `sys_user` WHERE `username`='po_user_223c');
SET @sm_id := (SELECT id FROM `sys_user` WHERE `username`='sm_user_223c');

-- 2) 插入团队
INSERT INTO `scrum_teams` (
  `team_name`, `header_id`, `team_description`, `team_status`, `is_deleted`
) VALUES
('Team-Alpha-223C', @po_id, 'Alpha 团队，负责SmartAdmin相关项目', 1, 0);

-- 取回团队ID
SET @team_id := (SELECT id FROM `scrum_teams` WHERE `team_name`='Team-Alpha-223C');

-- 3) 插入产品（最小必填+完整字段示例）
-- 产品A：最小必填，快速验证列表
INSERT INTO `scrum_products` (
  `product_name`, `product_code`, `product_owner_id`, `scrum_master_id`, `team_id`,
  `product_status`, `product_progress`, `is_deleted`
) VALUES
('SmartAdmin 平台', 'P-223C-0001', @po_id, @sm_id, @team_id, '2', 35, 0);

-- 产品B：完整字段，全面验证持久化
INSERT INTO `scrum_products` (
  `product_name`, `product_code`, `product_owner_id`, `scrum_master_id`, `team_id`,
  `product_source`, `product_status`, `product_progress`,
  `total_man_day`, `product_value`,
  `product_goal`, `user_story`, `acceptance_standard`, `boundary`,
  `stakeholders`, `customers`,
  `plan_start_date`, `plan_end_date`,
  `total_work_days`, `worked_days`,
  `product_remarks`, `is_deleted`
) VALUES
('数据中台', 'P-223C-0002', @po_id, @sm_id, @team_id,
  '内部立项', '1', 0,
  200.00, 500,
  '统一数据资产治理', '作为数据分析师，我希望统一口径', '满足质量规则与血缘追踪', 'DOD: 校验+压测',
  '架构、数据、测试', '企业内用户',
  '2025-11-01', '2026-03-31',
  90, 0,
  '立项阶段准备', 0
);

COMMIT;