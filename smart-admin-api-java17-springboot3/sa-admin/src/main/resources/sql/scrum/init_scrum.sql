-- =============================================
-- Scrum Activities 模块初始化脚本
-- =============================================

-- 1. 创建系统动态表
CREATE TABLE IF NOT EXISTS `sys_activities` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '动态ID（主键）',
  `user_id` bigint NOT NULL COMMENT '操作用户ID（关联sys_user.id）',
  `user_name` varchar(50) NOT NULL COMMENT '用户名（冗余，便于前端显示）',
  `action_type` varchar(20) NOT NULL COMMENT '操作类型（add=添加，delete=删除，update=修改，view=查看）',
  `target_type` varchar(50) NOT NULL COMMENT '操作对象类型（product=产品，sprint=Sprint，sprint_backlog=Sprint待办）',
  `target_id` bigint NOT NULL COMMENT '操作对象ID',
  `activity_content` text NOT NULL COMMENT '动态内容（如"Rick添加Sprint待办：9.23考试""李华删除产品：XX系统"）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target_type_id` (`target_type`, `target_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统动态表（全局操作记录）';

-- 2. 插入测试数据
INSERT INTO `sys_activities` (`user_id`, `user_name`, `action_type`, `target_type`, `target_id`, `activity_content`, `create_time`) VALUES
(1, '管理员', 'add', 'product', 1, '管理员创建了新产品：SmartAdmin系统', NOW()),
(1, '管理员', 'add', 'sprint', 1, '管理员创建了新的Sprint：2025年第一季度', NOW()),
(1, '管理员', 'add', 'sprint_backlog', 1, '管理员添加了Sprint待办：用户登录功能开发', NOW());

-- 3. 插入菜单（如果不存在）
INSERT IGNORE INTO `t_menu` (`menu_id`, `menu_name`, `parent_id`, `sort`, `path`, `component`, `icon`, `visible_flag`, `cache_flag`, `frame_flag`, `frame_url`, `deleted_flag`, `create_time`, `update_time`) VALUES
(1000, 'Scrum管理', 0, 1000, '/scrum', NULL, 'scrum', 1, 0, 0, NULL, 0, NOW(), NOW()),
(1001, '系统动态', 1000, 1001, '/scrum/activities', '/business/scrum/sys-activities/sys-activities-list', 'activity', 1, 0, 0, NULL, 0, NOW(), NOW()),
(1002, 'Scrum测试页', 1000, 1002, '/scrum/test', '/business/scrum/sys-activities/index', 'test', 1, 0, 0, NULL, 0, NOW(), NOW());

-- 4. 为管理员角色分配菜单权限
INSERT IGNORE INTO `t_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`) VALUES
(1, 1000, NOW(), NOW()),
(1, 1001, NOW(), NOW()),
(1, 1002, NOW(), NOW());

-- 5. 插入操作权限菜单
INSERT IGNORE INTO `t_menu` (`menu_id`, `menu_name`, `parent_id`, `sort`, `path`, `component`, `icon`, `visible_flag`, `cache_flag`, `frame_flag`, `frame_url`, `deleted_flag`, `create_time`, `update_time`) VALUES
(1003, '系统动态-查询', 1001, 1003, NULL, NULL, NULL, 0, 0, 0, NULL, 0, NOW(), NOW()),
(1004, '系统动态-新增', 1001, 1004, NULL, NULL, NULL, 0, 0, 0, NULL, 0, NOW(), NOW()),
(1005, '系统动态-编辑', 1001, 1005, NULL, NULL, NULL, 0, 0, 0, NULL, 0, NOW(), NOW()),
(1006, '系统动态-删除', 1001, 1006, NULL, NULL, NULL, 0, 0, 0, NULL, 0, NOW(), NOW());

-- 6. 为管理员角色分配操作权限
INSERT IGNORE INTO `t_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`) VALUES
(1, 1003, NOW(), NOW()),
(1, 1004, NOW(), NOW()),
(1, 1005, NOW(), NOW()),
(1, 1006, NOW(), NOW());

-- 7. 更新菜单排序（确保Scrum菜单显示在合适位置）
UPDATE `t_menu` SET `sort` = 1000 WHERE `menu_id` = 1000;
UPDATE `t_menu` SET `sort` = 1001 WHERE `menu_id` = 1001;
UPDATE `t_menu` SET `sort` = 1002 WHERE `menu_id` = 1002;
