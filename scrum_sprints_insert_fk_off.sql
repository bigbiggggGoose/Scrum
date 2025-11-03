-- =============================================
-- Scrum Teams & Sprints seed (FK OFF during insert)
-- 根据已插入的产品数据生成团队与迭代数据
-- =============================================

-- 1) 临时关闭外键约束
SET FOREIGN_KEY_CHECKS = 0;

-- 2) 取产品ID（依赖你已插入的两条产品数据）
SET @product_smartadmin_id := (SELECT id FROM `scrum_products` WHERE `product_code` = 'P-223C-0001' LIMIT 1);
SET @product_datacenter_id := (SELECT id FROM `scrum_products` WHERE `product_code` = 'P-223C-0002' LIMIT 1);

-- 如你在当前会话未设置这两个变量，可临时指定（按需取消注释）
-- SET @po_id := 1;        -- 产品负责人
-- SET @sm_id := 1;        -- Scrum Master

-- 3) 插入团队（用于满足迭代表的 NOT NULL team_id）
INSERT INTO `scrum_teams` (
  `team_name`, `header_id`, `team_description`, `notes`,
  `team_status`, `is_deleted`, `creator`
) VALUES
('Team223A', @sm_id, 'SmartAdmin 平台团队', '负责平台基础架构与权限模块', 1, 0, @sm_id),
('Team223B', @sm_id, '数据中台团队', '负责数据采集、治理与服务', 1, 0, @sm_id);

-- 拿到两个团队ID
SET @team_223a_id := (SELECT id FROM `scrum_teams` WHERE `team_name` = 'Team223A' LIMIT 1);
SET @team_223b_id := (SELECT id FROM `scrum_teams` WHERE `team_name` = 'Team223B' LIMIT 1);

-- 4) 为 SmartAdmin 平台 生成迭代（总人日与周期仅需小于“项目级”即可；此产品未设置项目周期与总人日，故按合理区间安排）
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`,
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`,
  `start_date`, `end_date`, `remark`, `creator`
) VALUES
('基础架构搭建', @product_smartadmin_id, @team_223a_id, '完成平台基础框架与项目脚手架',
 'completed', 100, 25.0, 200, '2025-10-01', '2025-10-10', '第一个迭代：框架与基础设施', @sm_id),
('用户管理模块', @product_smartadmin_id, @team_223a_id, '实现用户注册登录与权限体系',
 'completed', 100, 30.0, 240, '2025-10-11', '2025-10-25', '用户模块完成：RBAC/审计', @sm_id),
('系统设置模块', @product_smartadmin_id, @team_223a_id, '系统配置、菜单管理、组织结构',
 'in_progress', 60, 35.0, 168, '2025-10-26', '2025-11-15', '设置模块进行中', @sm_id),
('报表统计模块', @product_smartadmin_id, @team_223a_id, '统计报表与导出能力',
 'not_started', 0, 25.0, 0, '2025-11-16', '2025-11-30', '待开发', @sm_id);

-- 5) 为 数据中台 生成迭代（保证每个迭代的人日与周期均小于产品级：总人日=200；周期=2025-11-01~2026-03-31）
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`,
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`,
  `start_date`, `end_date`, `remark`, `creator`
) VALUES
('需求分析与架构设计', @product_datacenter_id, @team_223b_id, '完成需求调研、技术选型与总体架构',
 'completed', 100, 40.0, 320, '2025-11-01', '2025-11-20', '架构定版', @sm_id),
('数据采集层开发', @product_datacenter_id, @team_223b_id, '多源数据采集、清洗与标准化',
 'in_progress', 60, 45.0, 216, '2025-11-21', '2025-12-15', '采集层进行中', @sm_id),
('数据存储与治理', @product_datacenter_id, @team_223b_id, '数据仓库搭建、血缘追踪与质量规则',
 'not_started', 0, 50.0, 0, '2025-12-16', '2026-01-20', '待开发', @sm_id),
('数据服务与API', @product_datacenter_id, @team_223b_id, '数据查询接口与API网关',
 'not_started', 0, 35.0, 0, '2026-01-21', '2026-02-15', '待开发', @sm_id),
('可视化与监控', @product_datacenter_id, @team_223b_id, '大屏可视化与监控告警',
 'not_started', 0, 30.0, 0, '2026-02-16', '2026-03-15', '待开发', @sm_id);

-- 6) 恢复外键约束
SET FOREIGN_KEY_CHECKS = 1;

-- 7) 验证（可选）
-- SELECT s.id, s.sprint_name, p.product_name, t.team_name, s.sprint_status, s.sprint_progress,
--        s.total_man_day, s.consumed_hours, s.start_date, s.end_date
-- FROM scrum_sprints s
-- LEFT JOIN scrum_products p ON s.product_id = p.id
-- LEFT JOIN scrum_teams t ON s.team_id = t.id
-- ORDER BY p.product_name, s.start_date;