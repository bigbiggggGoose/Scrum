-- =============================================
-- Scrum 团队和迭代数据插入脚本
-- 基于已有产品数据生成相应的团队和迭代
-- =============================================

-- 设置变量（假设已有用户ID）
SET @admin_id = 1;  -- 管理员用户ID
SET @po_id = 1;     -- 产品负责人ID  
SET @sm_id = 1;     -- Scrum Master ID

-- =============================================
-- 1. 插入团队数据
-- =============================================

-- Team223A：负责SmartAdmin平台开发
INSERT INTO `scrum_teams` (
  `team_name`, `header_id`, `team_description`, `notes`, 
  `team_status`, `is_deleted`, `creator`
) VALUES 
('Team223A', @admin_id, 'SmartAdmin平台开发团队', '负责SmartAdmin平台的前后端开发与维护', 1, 0, @admin_id);

-- Team223B：负责数据中台开发  
INSERT INTO `scrum_teams` (
  `team_name`, `header_id`, `team_description`, `notes`,
  `team_status`, `is_deleted`, `creator`
) VALUES 
('Team223B', @admin_id, '数据中台开发团队', '负责数据中台的架构设计与开发实现', 1, 0, @admin_id);

-- 获取团队ID
SET @team_223a_id = LAST_INSERT_ID() - 1;  -- Team223A的ID
SET @team_223b_id = LAST_INSERT_ID();      -- Team223B的ID

-- =============================================
-- 2. 获取产品ID（基于已插入的产品数据）
-- =============================================

-- 获取SmartAdmin平台产品ID
SET @product_smartadmin_id = (SELECT id FROM scrum_products WHERE product_name = 'SmartAdmin 平台' LIMIT 1);

-- 获取数据中台产品ID  
SET @product_datacenter_id = (SELECT id FROM scrum_products WHERE product_name = '数据中台' LIMIT 1);

-- =============================================
-- 3. 插入SmartAdmin平台的迭代数据
-- =============================================

-- SmartAdmin平台 - 迭代1：基础架构搭建
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`, 
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`,
  `start_date`, `end_date`, `remark`, `creator`
) VALUES 
('基础架构搭建', @product_smartadmin_id, @team_223a_id, '完成SmartAdmin平台的基础架构设计与核心框架搭建',
 'completed', 100, 25.0, 200, '2025-09-01', '2025-09-15', '第一个迭代，已完成基础架构', @admin_id);

-- SmartAdmin平台 - 迭代2：用户管理模块
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`,
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`, 
  `start_date`, `end_date`, `remark`, `creator`
) VALUES 
('用户管理模块', @product_smartadmin_id, @team_223a_id, '实现用户注册、登录、权限管理等核心功能',
 'completed', 100, 30.0, 240, '2025-09-16', '2025-10-05', '用户管理模块开发完成', @admin_id);

-- SmartAdmin平台 - 迭代3：系统设置模块  
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`,
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`,
  `start_date`, `end_date`, `remark`, `creator`
) VALUES 
('系统设置模块', @product_smartadmin_id, @team_223a_id, '完成系统配置、菜单管理、角色权限等设置功能',
 'in_progress', 75, 28.0, 168, '2025-10-06', '2025-10-25', '系统设置模块开发中', @admin_id);

-- SmartAdmin平台 - 迭代4：报表统计模块
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`,
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`,
  `start_date`, `end_date`, `remark`, `creator`  
) VALUES 
('报表统计模块', @product_smartadmin_id, @team_223a_id, '实现数据统计、图表展示、报表导出等功能',
 'not_started', 0, 32.0, 0, '2025-10-26', '2025-11-15', '报表统计模块待开发', @admin_id);

-- =============================================
-- 4. 插入数据中台的迭代数据
-- =============================================

-- 数据中台 - 迭代1：需求分析与架构设计
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`,
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`,
  `start_date`, `end_date`, `remark`, `creator`
) VALUES 
('需求分析与架构设计', @product_datacenter_id, @team_223b_id, '完成数据中台的需求调研、技术选型和整体架构设计',
 'completed', 100, 40.0, 320, '2025-11-01', '2025-11-20', '需求分析和架构设计已完成', @admin_id);

-- 数据中台 - 迭代2：数据采集层开发
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`, 
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`,
  `start_date`, `end_date`, `remark`, `creator`
) VALUES 
('数据采集层开发', @product_datacenter_id, @team_223b_id, '实现多源数据采集、清洗、标准化处理功能',
 'in_progress', 60, 45.0, 216, '2025-11-21', '2025-12-15', '数据采集层开发进行中', @admin_id);

-- 数据中台 - 迭代3：数据存储与治理
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`,
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`, 
  `start_date`, `end_date`, `remark`, `creator`
) VALUES 
('数据存储与治理', @product_datacenter_id, @team_223b_id, '构建数据仓库、实现数据血缘追踪和质量监控',
 'not_started', 0, 50.0, 0, '2025-12-16', '2026-01-20', '数据存储与治理待开发', @admin_id);

-- 数据中台 - 迭代4：数据服务与API
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`,
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`,
  `start_date`, `end_date`, `remark`, `creator`
) VALUES 
('数据服务与API', @product_datacenter_id, @team_223b_id, '开发数据查询接口、实现数据权限控制和API网关',
 'not_started', 0, 35.0, 0, '2026-01-21', '2026-02-15', '数据服务与API待开发', @admin_id);

-- 数据中台 - 迭代5：可视化与监控
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`,
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`,
  `start_date`, `end_date`, `remark`, `creator`
) VALUES 
('可视化与监控', @product_datacenter_id, @team_223b_id, '构建数据可视化大屏、实现系统监控告警功能',
 'not_started', 0, 30.0, 0, '2026-02-16', '2026-03-15', '可视化与监控待开发', @admin_id);

-- =============================================
-- 5. 插入一些未分配团队的迭代（用于测试筛选功能）
-- =============================================

-- 创建一个临时产品用于测试
INSERT INTO `scrum_products` (
  `product_name`, `product_code`, `product_owner_id`, `scrum_master_id`, 
  `product_status`, `product_progress`, `is_deleted`, `creator`
) VALUES 
('移动端App', 'P-223C-0003', @po_id, @sm_id, '0', 0, 0, @admin_id);

SET @product_mobile_id = LAST_INSERT_ID();

-- 未分配团队的迭代1
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`,
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`,
  `start_date`, `end_date`, `remark`, `creator`
) VALUES 
('移动端原型设计', @product_mobile_id, NULL, '完成移动端App的原型设计和交互流程',
 'not_started', 0, 20.0, 0, '2026-04-01', '2026-04-15', '等待团队分配', @admin_id);

-- 未分配团队的迭代2  
INSERT INTO `scrum_sprints` (
  `sprint_name`, `product_id`, `team_id`, `sprint_goal`,
  `sprint_status`, `sprint_progress`, `total_man_day`, `consumed_hours`,
  `start_date`, `end_date`, `remark`, `creator`
) VALUES 
('移动端开发环境搭建', @product_mobile_id, NULL, '搭建React Native开发环境和CI/CD流水线',
 'not_started', 0, 15.0, 0, '2026-04-16', '2026-04-25', '等待团队分配', @admin_id);

-- =============================================
-- 6. 验证数据插入结果
-- =============================================

-- 查询插入的团队数据
SELECT 'Teams:' as info;
SELECT id, team_name, team_description, team_status FROM scrum_teams WHERE is_deleted = 0;

-- 查询插入的迭代数据  
SELECT 'Sprints:' as info;
SELECT 
  s.id,
  s.sprint_name,
  p.product_name,
  t.team_name,
  s.sprint_status,
  s.sprint_progress,
  s.total_man_day,
  s.consumed_hours,
  s.start_date,
  s.end_date
FROM scrum_sprints s
LEFT JOIN scrum_products p ON s.product_id = p.id  
LEFT JOIN scrum_teams t ON s.team_id = t.id
WHERE s.is_deleted = 0
ORDER BY p.product_name, s.start_date;

-- 统计信息
SELECT 'Summary:' as info;
SELECT 
  COUNT(*) as total_sprints,
  SUM(CASE WHEN team_id IS NULL THEN 1 ELSE 0 END) as unassigned_sprints,
  SUM(CASE WHEN sprint_status = 'completed' THEN 1 ELSE 0 END) as completed_sprints,
  SUM(CASE WHEN sprint_status = 'in_progress' THEN 1 ELSE 0 END) as in_progress_sprints,
  SUM(CASE WHEN sprint_status = 'not_started' THEN 1 ELSE 0 END) as not_started_sprints
FROM scrum_sprints 
WHERE is_deleted = 0;