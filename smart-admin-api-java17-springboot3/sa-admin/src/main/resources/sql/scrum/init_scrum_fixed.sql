-- =============================================
-- Scrum Activities Module Initialization Script
-- =============================================

-- 1. Create system activities table
CREATE TABLE IF NOT EXISTS `sys_activities` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Activity ID (Primary Key)',
  `user_id` bigint NOT NULL COMMENT 'User ID (Related to sys_user.id)',
  `user_name` varchar(50) NOT NULL COMMENT 'Username (Redundant for frontend display)',
  `action_type` varchar(20) NOT NULL COMMENT 'Action Type (add=add, delete=delete, update=update, view=view)',
  `target_type` varchar(50) NOT NULL COMMENT 'Target Type (product=product, sprint=Sprint, sprint_backlog=Sprint backlog)',
  `target_id` bigint NOT NULL COMMENT 'Target ID',
  `activity_content` text NOT NULL COMMENT 'Activity Content (e.g., "Rick added Sprint backlog: 9.23 exam", "Li Hua deleted product: XX system")',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Operation Time',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target_type_id` (`target_type`, `target_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='System Activities Table (Global Operation Records)';

-- 2. Insert test data
INSERT INTO `sys_activities` (`user_id`, `user_name`, `action_type`, `target_type`, `target_id`, `activity_content`, `create_time`) VALUES
(1, 'Administrator', 'add', 'product', 1, 'Administrator created new product: SmartAdmin System', NOW()),
(1, 'Administrator', 'add', 'sprint', 1, 'Administrator created new Sprint: Q1 2025', NOW()),
(1, 'Administrator', 'add', 'sprint_backlog', 1, 'Administrator added Sprint backlog: User login feature development', NOW());

-- 3. Insert menu items (if not exists)
INSERT IGNORE INTO `t_menu` (`menu_id`, `menu_name`, `parent_id`, `sort`, `path`, `component`, `icon`, `visible_flag`, `cache_flag`, `frame_flag`, `frame_url`, `deleted_flag`, `create_time`, `update_time`) VALUES
(1000, 'Scrum Management', 0, 1000, '/scrum', NULL, 'scrum', 1, 0, 0, NULL, 0, NOW(), NOW()),
(1001, 'System Activities', 1000, 1001, '/scrum/activities', '/business/scrum/sys-activities/sys-activities-list', 'activity', 1, 0, 0, NULL, 0, NOW(), NOW()),
(1002, 'Scrum Test Page', 1000, 1002, '/scrum/test', '/business/scrum/sys-activities/index', 'test', 1, 0, 0, NULL, 0, NOW(), NOW());

-- 4. Assign menu permissions to administrator role
INSERT IGNORE INTO `t_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`) VALUES
(1, 1000, NOW(), NOW()),
(1, 1001, NOW(), NOW()),
(1, 1002, NOW(), NOW());

-- 5. Insert operation permission menus
INSERT IGNORE INTO `t_menu` (`menu_id`, `menu_name`, `parent_id`, `sort`, `path`, `component`, `icon`, `visible_flag`, `cache_flag`, `frame_flag`, `frame_url`, `deleted_flag`, `create_time`, `update_time`) VALUES
(1003, 'System Activities-Query', 1001, 1003, NULL, NULL, NULL, 0, 0, 0, NULL, 0, NOW(), NOW()),
(1004, 'System Activities-Add', 1001, 1004, NULL, NULL, NULL, 0, 0, 0, NULL, 0, NOW(), NOW()),
(1005, 'System Activities-Edit', 1001, 1005, NULL, NULL, NULL, 0, 0, 0, NULL, 0, NOW(), NOW()),
(1006, 'System Activities-Delete', 1001, 1006, NULL, NULL, NULL, 0, 0, 0, NULL, 0, NOW(), NOW());

-- 6. Assign operation permissions to administrator role
INSERT IGNORE INTO `t_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`) VALUES
(1, 1003, NOW(), NOW()),
(1, 1004, NOW(), NOW()),
(1, 1005, NOW(), NOW()),
(1, 1006, NOW(), NOW());

-- 7. Update menu sorting (ensure Scrum menu displays in appropriate position)
UPDATE `t_menu` SET `sort` = 1000 WHERE `menu_id` = 1000;
UPDATE `t_menu` SET `sort` = 1001 WHERE `menu_id` = 1001;
UPDATE `t_menu` SET `sort` = 1002 WHERE `menu_id` = 1002;
