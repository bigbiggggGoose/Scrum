-- 系统动态表（全局操作记录）
CREATE TABLE `sys_activities` (
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
