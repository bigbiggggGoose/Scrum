-- 插入Scrum Activities菜单
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `parent_id`, `sort`, `path`, `component`, `icon`, `visible_flag`, `cache_flag`, `frame_flag`, `frame_url`, `deleted_flag`, `create_time`, `update_time`) VALUES
(1000, 'Scrum管理', 0, 1000, '/scrum', NULL, 'scrum', 1, 0, 0, NULL, 0, NOW(), NOW()),
(1001, '系统动态', 1000, 1001, '/scrum/activities', '/business/scrum/sys-activities/sys-activities-list', 'activity', 1, 0, 0, NULL, 0, NOW(), NOW());

-- 插入权限
INSERT INTO `t_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`) VALUES
(1, 1000, NOW(), NOW()),
(1, 1001, NOW(), NOW());

-- 插入操作权限
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `parent_id`, `sort`, `path`, `component`, `icon`, `visible_flag`, `cache_flag`, `frame_flag`, `frame_url`, `deleted_flag`, `create_time`, `update_time`) VALUES
(1002, '系统动态-查询', 1001, 1002, NULL, NULL, NULL, 0, 0, 0, NULL, 0, NOW(), NOW()),
(1003, '系统动态-新增', 1001, 1003, NULL, NULL, NULL, 0, 0, 0, NULL, 0, NOW(), NOW()),
(1004, '系统动态-编辑', 1001, 1004, NULL, NULL, NULL, 0, 0, 0, NULL, 0, NOW(), NOW()),
(1005, '系统动态-删除', 1001, 1005, NULL, NULL, NULL, 0, 0, 0, NULL, 0, NOW(), NOW());

-- 为管理员角色分配权限
INSERT INTO `t_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`) VALUES
(1, 1002, NOW(), NOW()),
(1, 1003, NOW(), NOW()),
(1, 1004, NOW(), NOW()),
(1, 1005, NOW(), NOW());
