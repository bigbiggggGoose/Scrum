-- ------------------------------
-- 前置配置：设置字符集与外键检查（导入前执行）
-- ------------------------------
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0; -- 临时关闭外键检查，避免创建中依赖报错（最后恢复）
SET sql_mode = 'NO_ENGINE_SUBSTITUTION'; -- 兼容不同MySQL版本的引擎配置


-- ------------------------------
-- 第一批次：完全无依赖的基础表（无外键，可任意顺序）
-- ------------------------------
-- 1. 地区表（无依赖，后续部门表关联它）
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area` (
  `id` VARCHAR(20) NOT NULL COMMENT '区域编码（主键）',
  `name` VARCHAR(64) DEFAULT NULL COMMENT '区域名称（如北京市、朝阳区）',
  `pid` VARCHAR(20) DEFAULT '0' COMMENT '上级区域ID（0=顶级区域）',
  `simplename` VARCHAR(64) DEFAULT NULL COMMENT '区域简称（如北京、朝阳）',
  `level` TINYINT DEFAULT NULL COMMENT '区域等级（1=省/直辖市，2=市，3=区/县）',
  `citycode` VARCHAR(16) DEFAULT NULL COMMENT '城市编码（如110000=北京市）',
  `zipcode` VARCHAR(16) DEFAULT NULL COMMENT '邮政编码',
  `mername` VARCHAR(128) DEFAULT NULL COMMENT '组合名称（如北京市朝阳区）',
  `lng` DECIMAL(10,6) DEFAULT NULL COMMENT '经度',
  `lat` DECIMAL(10,6) DEFAULT NULL COMMENT '纬度',
  `pinyin` VARCHAR(64) DEFAULT NULL COMMENT '区域拼音（如Beijing）',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_pid` (`pid`) COMMENT '优化区域树形查询'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地区表（省/市/区三级联动）';

-- 2. 系统配置表（无依赖）
DROP TABLE IF EXISTS `sys_system_config`;
CREATE TABLE `sys_system_config` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '配置ID（主键）',
  `site_title` VARCHAR(100) DEFAULT NULL COMMENT '网站标题（如Scrum项目管理系统）',
  `system_name` VARCHAR(100) DEFAULT NULL COMMENT '系统名称',
  `site_logo` VARCHAR(255) DEFAULT NULL COMMENT '网站图标URL',
  `icp_number` VARCHAR(100) DEFAULT NULL COMMENT '备案号（如京ICP备xxxx号）',
  `footer_content` TEXT DEFAULT NULL COMMENT '底部内容（如版权信息）',
  `mail_server` VARCHAR(100) DEFAULT NULL COMMENT '邮箱服务器（如smtp.qq.com）',
  `mail_port` INT DEFAULT NULL COMMENT '邮箱端口（如465）',
  `mail_from` VARCHAR(100) DEFAULT NULL COMMENT '发送邮箱（如xxx@qq.com）',
  `mail_user` VARCHAR(50) DEFAULT NULL COMMENT '邮箱账号',
  `mail_password` VARCHAR(100) DEFAULT NULL COMMENT '邮箱授权码（非登录密码）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网站与邮箱配置表';

-- 3. 节假日表（无依赖）
DROP TABLE IF EXISTS `sys_holidays`;
CREATE TABLE `sys_holidays` (
  `id` VARCHAR(4) NOT NULL COMMENT '年份（主键，如2025）',
  `holiday_list` TEXT NOT NULL COMMENT '节假日日期列表（JSON格式，如["2025-01-01","2025-02-10"]）',
  `workday_list` TEXT DEFAULT NULL COMMENT '补班日期列表（JSON格式，如["2025-02-08","2025-02-09"]）',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='节假日配置表（按年份存储）';

-- 4. 数据字典主表（无依赖，后续字典选项表关联它）
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典ID（主键）',
  `dict_name` VARCHAR(100) NOT NULL COMMENT '字典名称（如产品状态、Sprint状态）',
  `dict_code` VARCHAR(100) NOT NULL COMMENT '字典编号（唯一，如product_status）',
  `dict_type` INT(11) NOT NULL COMMENT '类型（1=选项，2=属性值，3=系统属性，4=邮箱设置）',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
  `is_disable` INT(11) NOT NULL DEFAULT 2 COMMENT '是否禁用（1=是，2=否）',
  `status` TINYINT DEFAULT 1 COMMENT '状态（1=启用，0=禁用）',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_code` (`dict_code`),
  KEY `idx_dict_name` (`dict_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典主表';

-- 5. 权限表（修复语句截断问题，无依赖）
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID（主键）',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父权限ID（0=顶级）',
  `perm_code` VARCHAR(100) NOT NULL COMMENT '权限标识（如sys:user:add）',
  `perm_name` VARCHAR(100) NOT NULL COMMENT '权限名称（如新增用户）',
  `menu_type` TINYINT NOT NULL COMMENT '菜单类型（0=目录，1=菜单，2=按钮）',
  `level` INT(11) NOT NULL DEFAULT 1 COMMENT '权限层级（1=一级菜单，2=二级菜单，3=按钮）',
  `icon` VARCHAR(500) DEFAULT NULL COMMENT '图标（如el-icon-s-tools）',
  `url` VARCHAR(200) DEFAULT NULL COMMENT '路由URL（如/user/list）',
  `component` VARCHAR(200) DEFAULT NULL COMMENT '前端组件路径（如/views/user/index）',
  `open_type` INT(11) DEFAULT 1 COMMENT '打开方式（1=内部，2=外部）',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态（1=启用，0=禁用）',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_perm_code` (`perm_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_menu_type` (`menu_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限管理表';

-- 6. 部门表（仅依赖sys_area，已创建）
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID（主键）',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID（0=顶级部门）',
  `dept_code` VARCHAR(50) NOT NULL COMMENT '部门标识（唯一）',
  `dept_name` VARCHAR(100) NOT NULL COMMENT '部门名称',
  `order_num` INT DEFAULT 0 COMMENT '排序（数值越小越靠前）',
  `level` INT(11) NOT NULL DEFAULT 1 COMMENT '部门等级（1=总部，2=分公司，3=部门）',
  `dptarea` VARCHAR(255) DEFAULT NULL COMMENT '关联区域ID（多个用逗号分隔，关联sys_area.id）',
  `status` TINYINT DEFAULT 1 COMMENT '状态（1=正常，0=停用）',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dept_code` (`dept_code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门管理表';

-- 7. 角色表（无依赖，后续用户角色、角色权限、角色部门关联表关联它）
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID（主键）',
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色标识（唯一，如ROLE_ADMIN）',
  `role_name` VARCHAR(100) NOT NULL COMMENT '角色名称（如超级管理员）',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父角色ID（0=顶级角色）',
  `order_num` INT DEFAULT 0 COMMENT '排序',
  `level` INT(11) NOT NULL DEFAULT 1 COMMENT '角色等级',
  `jxfm` TEXT DEFAULT NULL COMMENT '绩效公式',
  `dy` VARCHAR(255) DEFAULT NULL COMMENT '待遇描述',
  `status` TINYINT DEFAULT 1 COMMENT '状态（1=启用，0=禁用）',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`),
  KEY `idx_status` (`status`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色管理表';


-- ------------------------------
-- 第二批次：依赖“第一批次表”的基础关联表
-- ------------------------------
-- 1. 数据字典选项表（依赖sys_dict，已创建）
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '选项ID（主键）',
  `dict_id` BIGINT NOT NULL COMMENT '字典ID（关联sys_dict.id）',
  `item_key` VARCHAR(32) NOT NULL COMMENT '选项名称',
  `item_value` VARCHAR(32) NOT NULL COMMENT '选项值',
  `sort_order` INT DEFAULT 0 COMMENT '排序（数值越小越靠前）',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
  `is_disable` INT(11) NOT NULL DEFAULT 2 COMMENT '是否禁用（1=是，2=否）',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_dict_id` (`dict_id`),
  CONSTRAINT `fk_dict_item_dict` FOREIGN KEY (`dict_id`) REFERENCES `sys_dict` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典选项表';

-- 2. 角色权限关联表（依赖sys_role、sys_permission，均已创建）
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID（主键）',
  `role_id` BIGINT NOT NULL COMMENT '角色ID（关联sys_role.id）',
  `perm_id` BIGINT NOT NULL COMMENT '权限ID（关联sys_permission.id）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_perm` (`role_id`, `perm_id`),
  CONSTRAINT `fk_role_perm_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_role_perm_perm` FOREIGN KEY (`perm_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 3. 用户表（依赖sys_dept、sys_role，均已创建）
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID（主键，数值型）',
  `username` VARCHAR(50) NOT NULL COMMENT '登录名（唯一）',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（加密存储，如BCrypt）',
  `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
  `sidx` TINYINT(4) DEFAULT NULL COMMENT '工资表顺序',
  `avatar_url` TEXT DEFAULT NULL COMMENT '头像URL（支持长文本存储）',
  `birth` DATE DEFAULT NULL COMMENT '生日',
  `sex` INT(11) DEFAULT NULL COMMENT '性别（1=男，2=女）',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号（唯一）',
  `role_id` BIGINT DEFAULT NULL COMMENT '角色ID（关联sys_role.id，数值型匹配）',
  `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID（关联sys_dept.id，数值型匹配）',
  `wxapp_uid` VARCHAR(32) DEFAULT NULL COMMENT '微信小程序用户ID',
  `wxapp_openid` VARCHAR(32) DEFAULT NULL COMMENT '微信小程序OpenID',
  `is_disable` INT(11) NOT NULL DEFAULT 2 COMMENT '是否禁用（1=是，2=否）',
  `status` TINYINT DEFAULT 1 COMMENT '状态（1=在职，0=离职）',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_wxapp_openid` (`wxapp_openid`),
  UNIQUE KEY `uk_name_phone` (`real_name`, `phone`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_dept_id` (`dept_id`),
  CONSTRAINT `fk_user_dept` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 4. 角色部门关联表（依赖sys_role、sys_dept，均已创建）
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID（主键）',
  `role_id` BIGINT NOT NULL COMMENT '角色ID（关联sys_role.id）',
  `dept_id` BIGINT NOT NULL COMMENT '部门ID（关联sys_dept.id）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_dept` (`role_id`, `dept_id`),
  CONSTRAINT `fk_role_dept_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_role_dept_dept` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色部门关联表';

-- 5. 用户角色关联表（依赖sys_user、sys_role，均已创建）
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID（主键）',
  `user_id` BIGINT NOT NULL COMMENT '用户ID（关联sys_user.id）',
  `role_id` BIGINT NOT NULL COMMENT '角色ID（关联sys_role.id）',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 6. 第三方账号表（依赖sys_user，已创建）
DROP TABLE IF EXISTS `sys_third_account`;
CREATE TABLE `sys_third_account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '关联系统用户ID（sys_user.id，数值型匹配）',
  `openid` VARCHAR(200) NOT NULL COMMENT '第三方账号OpenID（如微信UnionID）',
  `account_type` INT(11) NOT NULL COMMENT '账号类型（1=微信，2=QQ，3=阿里）',
  `bind_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  `unbind_time` TIMESTAMP NULL DEFAULT NULL COMMENT '解绑时间（NULL=未解绑）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_type` (`user_id`, `account_type`),
  CONSTRAINT `fk_third_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方账号绑定表';

-- 7. 模板表（依赖sys_user，已创建）
DROP TABLE IF EXISTS `sys_templates`;
CREATE TABLE `sys_templates` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `template_code` VARCHAR(255) NOT NULL COMMENT '模板标识（唯一，如sprint_start_notice）',
  `template_type` INT(11) NOT NULL COMMENT '类型（1=站内信，2=邮件）',
  `template_name` VARCHAR(255) NOT NULL COMMENT '模板名称（如Sprint启动通知模板）',
  `template_content` TEXT NOT NULL COMMENT '模板内容（支持变量占位符，如${sprintName}）',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_code` (`template_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='站内信/邮件模板表';

-- 8. 登录日志表（依赖sys_user，已创建）
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID（主键）',
  `user_id` BIGINT NOT NULL COMMENT '用户ID（关联sys_user.id，数值型匹配）',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名（冗余，便于查询）',
  `login_ip` VARCHAR(45) DEFAULT NULL COMMENT '登录IP（支持IPv6）',
  `client_info` VARCHAR(500) DEFAULT NULL COMMENT '客户端信息（如Mozilla/5.0 (Windows NT 10.0; Win64; x64)）',
  `login_time` DATETIME NOT NULL COMMENT '登录时间',
  `login_result` INT(11) NOT NULL COMMENT '登录结果（1=成功，2=失败）',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_login_time` (`login_time`),
  CONSTRAINT `fk_login_log_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录日志表';

-- 9. 操作日志表（依赖sys_user，已创建）
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID（主键）',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID（关联sys_user.id，数值型匹配）',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名（冗余）',
  `ip_address` VARCHAR(45) DEFAULT NULL COMMENT '操作IP',
  `client_info` VARCHAR(255) DEFAULT NULL COMMENT '客户端信息',
  `module` VARCHAR(32) DEFAULT NULL COMMENT '操作模块（如产品管理、Sprint管理）',
  `operation` VARCHAR(32) DEFAULT NULL COMMENT '操作类型（如添加、删除）',
  `opt_time` DATETIME NOT NULL COMMENT '操作时间',
  `log_type` INT(11) DEFAULT NULL COMMENT '日志类型（1=业务，2=错误信息）',
  `uri` VARCHAR(200) DEFAULT NULL COMMENT '操作接口URL（如/api/product/add）',
  `content` LONGTEXT DEFAULT NULL COMMENT '操作详情（如“删除Sprint待办：9.23考试”）',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  KEY `idx_user_module` (`user_id`, `module`),
  KEY `idx_opt_time` (`opt_time`),
  CONSTRAINT `fk_operation_log_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';

-- 10. 团队表（修复header_id类型，依赖sys_user，已创建）
DROP TABLE IF EXISTS `scrum_teams`;
CREATE TABLE `scrum_teams` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '团队ID（主键）',
  `team_name` VARCHAR(255) NOT NULL COMMENT '团队名称（如Team223C、软件223第七组）',
  `header_id` BIGINT DEFAULT NULL COMMENT '团队负责人ID（关联sys_user.id，数值型匹配）',
  `team_description` TEXT DEFAULT NULL COMMENT '团队描述（如负责数据库项目开发、前端组）',
  `notes` VARCHAR(1024) DEFAULT NULL COMMENT '团队说明',
  `team_status` TINYINT(1) DEFAULT 1 COMMENT '状态（1=启用，0=禁用）',
  `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_team_name` (`team_name`),
  KEY `idx_team_status` (`team_status`),
  CONSTRAINT `fk_team_header` FOREIGN KEY (`header_id`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='团队表';


-- ------------------------------
-- 第三批次：团队管理层（依赖“第二批次表”）
-- ------------------------------
-- 1. 团队成员关联表（依赖scrum_teams、sys_user、sys_dept，均已创建）
DROP TABLE IF EXISTS `scrum_team_members`;
CREATE TABLE `scrum_team_members` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID（主键）',
  `team_id` BIGINT NOT NULL COMMENT '团队ID（关联scrum_teams.id）',
  `user_id` BIGINT NOT NULL COMMENT '用户ID（关联sys_user.id，数值型匹配）',
  `sprint_id` BIGINT DEFAULT NULL COMMENT 'Sprint ID（暂不依赖，后续Sprint表创建后可补）',
  `member_role` ENUM('SM', 'PO', 'DEV', 'TEST') NOT NULL COMMENT '成员角色：SM=Scrum Master，PO=产品负责人，DEV=开发，TEST=测试',
  `post` VARCHAR(32) DEFAULT NULL COMMENT '岗位（如前端开发、后端开发）',
  `dept_id` BIGINT DEFAULT NULL COMMENT '所属部门（关联sys_dept.id，数值型匹配）',
  `name` VARCHAR(25) DEFAULT NULL COMMENT '成员姓名（冗余）',
  `sex` SMALLINT(6) DEFAULT NULL COMMENT '性别（1=男，2=女）',
  `start_time` DATE NOT NULL COMMENT '加入团队时间',
  `end_time` DATE DEFAULT NULL COMMENT '离开团队时间（NULL=在职）',
  `hour4day` TINYINT(4) DEFAULT 8 COMMENT '每日可用工时数（默认8小时）',
  `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否在团队中（1=是，0=否）',
  `notes` VARCHAR(255) DEFAULT NULL COMMENT '备注（如擅长Java、负责数据库设计）',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_team_user` (`team_id`, `user_id`) COMMENT '同一用户在同一团队仅1条记录',
  KEY `idx_member_role` (`member_role`),
  KEY `idx_is_active` (`is_active`),
  CONSTRAINT `fk_team_member_team` FOREIGN KEY (`team_id`) REFERENCES `scrum_teams` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_team_member_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_team_member_dept` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='团队成员关联表';


-- ------------------------------
-- 第四批次：Scrum核心层（依赖“团队管理层”）
-- ------------------------------
-- 1. 产品表（依赖sys_user、scrum_teams，均已创建）
DROP TABLE IF EXISTS `scrum_products`;
CREATE TABLE `scrum_products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '产品ID（主键）',
  `product_name` VARCHAR(255) NOT NULL COMMENT '产品名称',
  `product_code` VARCHAR(100) NOT NULL COMMENT '产品编号（唯一）',
  `product_owner_id` BIGINT NOT NULL COMMENT '产品负责人（PO，关联sys_user.id，数值型匹配）',
  `scrum_master_id` BIGINT NOT NULL COMMENT 'Scrum Master（关联sys_user.id，数值型匹配）',
  `team_id` BIGINT NOT NULL COMMENT '负责团队ID（关联scrum_teams.id）',
  `product_source` VARCHAR(50) DEFAULT NULL COMMENT '产品来源（如客户需求、内部立项）',
  `product_status` VARCHAR(10) NOT NULL COMMENT '状态（1=未开始，2=进行中，3=未完成，4=已完成，5=已关闭，6=已挂起，7=已延期）',
  `product_progress` SMALLINT(6) NOT NULL DEFAULT 0 COMMENT '产品进度（0-100）',
  `total_man_day` DECIMAL(8,2) DEFAULT NULL COMMENT '总人日',
  `product_value` DECIMAL(10,0) DEFAULT NULL COMMENT '产品价值',
  `product_goal` TEXT DEFAULT NULL COMMENT '产品目标',
  `user_story` TEXT DEFAULT NULL COMMENT '用户故事',
  `acceptance_standard` TEXT DEFAULT NULL COMMENT '验收标准',
  `boundary` TEXT DEFAULT NULL COMMENT '边界设定（DOD）',
  `stakeholders` TEXT DEFAULT NULL COMMENT '干系人',
  `customers` VARCHAR(200) DEFAULT NULL COMMENT '用户和客户',
  `plan_start_date` DATE DEFAULT NULL COMMENT '计划开始日期',
  `plan_end_date` DATE DEFAULT NULL COMMENT '计划结束日期',
  `total_work_days` DECIMAL(10,0) DEFAULT NULL COMMENT '项目所需总天数',
  `worked_days` DECIMAL(10,0) DEFAULT NULL COMMENT '已工作天数',
  `product_remarks` TEXT DEFAULT NULL COMMENT '产品备注',
  `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除（0=未删，1=已删）',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_code` (`product_code`),
  KEY `idx_product_owner` (`product_owner_id`),
  KEY `idx_scrum_master` (`scrum_master_id`),
  KEY `idx_team_id` (`team_id`),
  KEY `idx_product_status` (`product_status`),
  CONSTRAINT `fk_product_po` FOREIGN KEY (`product_owner_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_product_sm` FOREIGN KEY (`scrum_master_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_product_team` FOREIGN KEY (`team_id`) REFERENCES `scrum_teams` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

-- 2. Sprint表（修复sprint_status字段，依赖scrum_products、scrum_teams，均已创建）
DROP TABLE IF EXISTS `scrum_sprints`;
CREATE TABLE `scrum_sprints` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Sprint ID（主键）',
  `sprint_name` VARCHAR(255) NOT NULL COMMENT 'Sprint名称（如数据库构建、系统设计）',
  `product_id` BIGINT NOT NULL COMMENT '所属产品ID（关联scrum_products.id）',
  `team_id` BIGINT NOT NULL COMMENT '负责团队ID（关联scrum_teams.id）',
  `sprint_goal` TEXT DEFAULT NULL COMMENT 'Sprint目标',
  `sprint_status` VARCHAR(20) DEFAULT 'not_started' COMMENT '迭代状态（not_started=未开始，in_progress=进行中，unfinished=未完成，completed=已完成，closed=已关闭，suspended=已挂起，delayed=已延期）',
  `sprint_progress` SMALLINT(6) DEFAULT 0 COMMENT '迭代进度（0-100）',
  `total_man_day` DECIMAL(8,2) DEFAULT NULL COMMENT '总人日',
  `consumed_hours` INT UNSIGNED DEFAULT 0 COMMENT '已消耗工时（小时）',
  `start_date` DATE NOT NULL COMMENT '开始时间',
  `end_date` DATE NOT NULL COMMENT '结束时间',
  `remark` TEXT DEFAULT NULL COMMENT '备注',
  `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_team_id` (`team_id`),
  KEY `idx_sprint_status` (`sprint_status`),
  CONSTRAINT `fk_sprint_product` FOREIGN KEY (`product_id`) REFERENCES `scrum_products` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_sprint_team` FOREIGN KEY (`team_id`) REFERENCES `scrum_teams` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目迭代表';

-- 3. 成员参与Sprint表（依赖scrum_sprints、scrum_team_members，均已创建）
DROP TABLE IF EXISTS `scrum_member_sprints`;
CREATE TABLE `scrum_member_sprints` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID（主键）',
  `sprint_id` BIGINT NOT NULL COMMENT 'Sprint ID（关联scrum_sprints.id）',
  `team_member_id` BIGINT NOT NULL COMMENT '团队成员ID（关联scrum_team_members.id）',
  `start_date` DATE NOT NULL COMMENT '成员参与Sprint开始时间',
  `end_date` DATE NOT NULL COMMENT '成员参与Sprint结束时间',
  `work_hours_per_day` INT UNSIGNED DEFAULT 8 COMMENT '每日工时（默认8小时）',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sprint_teammember` (`sprint_id`, `team_member_id`) COMMENT '同一成员在同一Sprint仅1条记录',
  CONSTRAINT `fk_member_sprint_sprint` FOREIGN KEY (`sprint_id`) REFERENCES `scrum_sprints` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_member_sprint_member` FOREIGN KEY (`team_member_id`) REFERENCES `scrum_team_members` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成员参与Sprint表';

-- 4. 产品待办项表（依赖scrum_products，已创建）
DROP TABLE IF EXISTS `scrum_product_backlogs`;
CREATE TABLE `scrum_product_backlogs` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '待办ID（主键）',
  `product_id` BIGINT NOT NULL COMMENT '所属产品ID（关联scrum_products.id）',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父待办ID（0=顶层，支撑树形结构）',
  `backlog_name` VARCHAR(512) NOT NULL COMMENT '待办名称（用户故事标题）',
  `priority` SMALLINT(6) NOT NULL COMMENT '优先级（数字越大优先级越低）',
  `man_day` DOUBLE(8,1) DEFAULT NULL COMMENT '人日',
  `content` VARCHAR(2048) DEFAULT NULL COMMENT '待办内容',
  `level` INT(11) DEFAULT 1 COMMENT '分级（最大4级）',
  `cell_style` INT(11) DEFAULT NULL COMMENT '单元格样式：1=靠左白背景，2=居中白背景，3=居中加粗白背景，4=居中加粗蓝背景（#b7dee8）等',
  `backlog_weight` INT DEFAULT 0 COMMENT '同级排序权重（数值越小越靠前）',
  `estimated_hours` INT UNSIGNED DEFAULT 0 COMMENT '预估工时（小时）',
  `backlog_status` VARCHAR(50) DEFAULT 'todo' COMMENT '状态（关联sys_dict：todo=待办，in_progress=进行中，completed=已完成）',
  `backlog_remarks` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_backlog_status` (`backlog_status`),
  CONSTRAINT `fk_pbacklog_product` FOREIGN KEY (`product_id`) REFERENCES `scrum_products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品待办事项表';

-- 5. 产品与Sprint待办中间表（依赖scrum_sprints、scrum_product_backlogs，均已创建）
DROP TABLE IF EXISTS `scrum_product_sprint_backlog`;
CREATE TABLE `scrum_product_sprint_backlog` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sprint_id` BIGINT NOT NULL COMMENT 'Sprint项目ID（关联scrum_sprints.id）',
  `backlog_id` BIGINT NOT NULL COMMENT '产品待办ID（关联scrum_product_backlogs.id）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sprint_backlog` (`sprint_id`, `backlog_id`),
  CONSTRAINT `fk_product_sprint_sprint` FOREIGN KEY (`sprint_id`) REFERENCES `scrum_sprints` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_product_sprint_backlog` FOREIGN KEY (`backlog_id`) REFERENCES `scrum_product_backlogs` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Sprint与产品待办中间表';

-- 6. 会议表（修复moderator_id类型，依赖scrum_sprints、sys_user，均已创建）
DROP TABLE IF EXISTS `scrum_meetings`;
CREATE TABLE `scrum_meetings` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '会议ID（主键）',
  `sprint_id` BIGINT NOT NULL COMMENT 'Sprint项目ID（关联scrum_sprints.id）',
  `meeting_name` VARCHAR(100) DEFAULT NULL COMMENT '会议名称（如“Sprint 1 计划会议”）',
  `meeting_time` DATETIME DEFAULT NULL COMMENT '会议时间',
  `meeting_type` INT(11) DEFAULT NULL COMMENT '会议类型（1=站会，2=计划会议，3=审视会议，4=回顾会议，5=评审会议）',
  `moderator_id` BIGINT DEFAULT NULL COMMENT '主持人ID（关联sys_user.id，数值型匹配）',
  `participants` VARCHAR(255) DEFAULT NULL COMMENT '参会人ID列表（逗号分隔，存储sys_user.id，如“1,2,3”）',
  `meeting_state` INT(11) DEFAULT NULL COMMENT '执行状态（1=未开始，2=进行中，3=未完成，4=已完成，5=已关闭，6=已挂起，7=已延期）',
  `meeting_objective` TEXT DEFAULT NULL COMMENT '会议目的',
  `meeting_summary` TEXT DEFAULT NULL COMMENT '会议纪要',
  `submit_time` DATETIME DEFAULT NULL COMMENT '会议纪要提交时间',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  KEY `idx_sprint_id` (`sprint_id`),
  KEY `idx_meeting_time` (`meeting_time`),
  KEY `idx_moderator_id` (`moderator_id`),
  CONSTRAINT `fk_meeting_sprint` FOREIGN KEY (`sprint_id`) REFERENCES `scrum_sprints` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_meeting_moderator` FOREIGN KEY (`moderator_id`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议表';

-- 7. 审视计划表（修复module_name长度，依赖scrum_sprints、scrum_meetings、scrum_teams，均已创建）
DROP TABLE IF EXISTS `scrum_review_plans`;
CREATE TABLE `scrum_review_plans` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '审视ID（主键）',
  `sprint_id` BIGINT NOT NULL COMMENT 'Sprint项目ID（关联scrum_sprints.id）',
  `meeting_id` BIGINT DEFAULT NULL COMMENT '会议记录ID（关联scrum_meetings.id，数值型匹配）',
  `team_id` BIGINT DEFAULT NULL COMMENT '团队ID（关联scrum_teams.id，数值型匹配）',
  `module_name` VARCHAR(50) DEFAULT NULL COMMENT '模块名称（如登录模块、订单模块）',
  `review_state` VARCHAR(1) DEFAULT NULL COMMENT '审视状态（0=未审视，1=完成，2=待修改）',
  `bug_count` INT(11) DEFAULT NULL COMMENT 'Bug数量',
  `planned_start_time` DATETIME DEFAULT NULL COMMENT '计划开始时间',
  `planned_end_time` DATETIME DEFAULT NULL COMMENT '计划完成时间',
  `actual_start_time` DATETIME DEFAULT NULL COMMENT '实际开始时间',
  `actual_end_time` DATETIME DEFAULT NULL COMMENT '实际结束时间',
  `review_need` TEXT DEFAULT NULL COMMENT '审视计划描述',
  `attachment_count` INT(4) DEFAULT NULL COMMENT '附件数量',
  `review_conclusion` TEXT DEFAULT NULL COMMENT '审视结论描述',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  KEY `idx_sprint_id` (`sprint_id`),
  KEY `idx_meeting_id` (`meeting_id`),
  CONSTRAINT `fk_review_sprint` FOREIGN KEY (`sprint_id`) REFERENCES `scrum_sprints` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_review_meeting` FOREIGN KEY (`meeting_id`) REFERENCES `scrum_meetings` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_review_team` FOREIGN KEY (`team_id`) REFERENCES `scrum_teams` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审视表';

-- 8. Sprint待办项表（依赖scrum_sprints、scrum_product_backlogs、scrum_team_members、scrum_review_plans，均已创建）
DROP TABLE IF EXISTS `scrum_sprint_backlogs`;
CREATE TABLE `scrum_sprint_backlogs` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '待办ID（主键）',
  `sprint_id` BIGINT NOT NULL COMMENT '所属Sprint ID（关联scrum_sprints.id）',
  `product_backlog_id` BIGINT DEFAULT NULL COMMENT '关联产品待办ID（关联scrum_product_backlogs.id）',
  `team_member_id` BIGINT DEFAULT NULL COMMENT '团队成员ID（关联scrum_team_members.id，数值型匹配）',
  `backlog_title` VARCHAR(255) NOT NULL COMMENT '任务标题（如9.23考试、接口开发）',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父节点ID（0=顶层）',
  `child_node` VARCHAR(255) DEFAULT NULL COMMENT '子节点ID列表（逗号分隔，存储scrum_sprint_backlogs.id，如“1001,1002”）',
  `backlog_status` VARCHAR(11) DEFAULT NULL COMMENT '执行状态（1=未开始，2=进行中，3=已完成）',
  `backlog_goal` TEXT DEFAULT NULL COMMENT '任务目标',
  `estimated_hours` DOUBLE(4,1) DEFAULT NULL COMMENT '预计工时',
  `original_estimated_hours` DOUBLE(4,1) DEFAULT NULL COMMENT '原始预计工时',
  `actual_hours` DOUBLE(4,1) DEFAULT NULL COMMENT '实际消耗工时',
  `completed_hours` DOUBLE(4,1) DEFAULT NULL COMMENT '完成工时',
  `planned_start_time` DATETIME DEFAULT NULL COMMENT '计划开始时间',
  `planned_end_time` DATETIME DEFAULT NULL COMMENT '计划完成时间',
  `actual_start_time` DATETIME DEFAULT NULL COMMENT '实际开始时间',
  `actual_end_time` DATETIME DEFAULT NULL COMMENT '实际结束时间',
  `survey_id` BIGINT DEFAULT NULL COMMENT '关联审视计划ID（scrum_review_plans.id，数值型匹配）',
  `backlog_progress` DECIMAL(4,1) DEFAULT NULL COMMENT '任务进度（0-100）',
  `backlog_description` TEXT DEFAULT NULL COMMENT '任务描述',
  `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  KEY `idx_sprint_id` (`sprint_id`),
  KEY `idx_product_backlog` (`product_backlog_id`),
  KEY `idx_team_member` (`team_member_id`),
  CONSTRAINT `fk_sbacklog_sprint` FOREIGN KEY (`sprint_id`) REFERENCES `scrum_sprints` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_sbacklog_pbacklog` FOREIGN KEY (`product_backlog_id`) REFERENCES `scrum_product_backlogs` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_sbacklog_member` FOREIGN KEY (`team_member_id`) REFERENCES `scrum_team_members` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_sbacklog_survey` FOREIGN KEY (`survey_id`) REFERENCES `scrum_review_plans` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Sprint待办事项表';

-- 9. 开发记录表（依赖scrum_sprint_backlogs、scrum_team_members，均已创建）
DROP TABLE IF EXISTS `scrum_development_records`;
CREATE TABLE `scrum_development_records` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID（主键）',
  `product_id` BIGINT DEFAULT NULL COMMENT '产品ID（关联scrum_products.id，数值型匹配）',
  `sprint_id` BIGINT DEFAULT NULL COMMENT 'Sprint项目ID（关联scrum_sprints.id，数值型匹配）',
  `sprint_backlog_id` BIGINT DEFAULT NULL COMMENT 'Sprint待办ID（关联scrum_sprint_backlogs.id，数值型匹配）',
  `team_member_id` BIGINT DEFAULT NULL COMMENT '团队成员ID（关联scrum_team_members.id，数值型匹配）',
  `work_date` DATETIME DEFAULT NULL COMMENT '工作日期',
  `completed_hours` DOUBLE(4,1) DEFAULT NULL COMMENT '完成工时',
  `actual_hours` DOUBLE(4,1) DEFAULT NULL COMMENT '实际消耗工时',
  `development_progress` DECIMAL(8,5) DEFAULT NULL COMMENT '开发进度（0-1）',
  `daily_plan` TEXT DEFAULT NULL COMMENT '当日计划',
  `planned_hours` INT(4) DEFAULT NULL COMMENT '计划工时',
  `daily_summary` VARCHAR(255) DEFAULT NULL COMMENT '当日总结',
  `remark` TEXT DEFAULT NULL COMMENT '备注',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  KEY `idx_sprint_backlog` (`sprint_backlog_id`),
  KEY `idx_team_member_date` (`team_member_id`, `work_date`),
  CONSTRAINT `fk_develop_record_sbacklog` FOREIGN KEY (`sprint_backlog_id`) REFERENCES `scrum_sprint_backlogs` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_develop_record_member` FOREIGN KEY (`team_member_id`) REFERENCES `scrum_team_members` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开发记录表';

-- 10. 审视记录表（依赖scrum_review_plans、scrum_sprint_backlogs，均已创建）
DROP TABLE IF EXISTS `scrum_review_records`;
CREATE TABLE `scrum_review_records` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sprint_backlog_id` BIGINT DEFAULT NULL COMMENT 'Sprint待办ID（关联scrum_sprint_backlogs.id，数值型匹配）',
  `new_backlog_id` BIGINT DEFAULT NULL COMMENT '修复Bug的新待办ID（关联scrum_sprint_backlogs.id，数值型匹配）',
  `review_id` BIGINT DEFAULT NULL COMMENT '审视ID（关联scrum_review_plans.id，数值型匹配）',
  `bug_count` INT(11) DEFAULT NULL COMMENT '待办审视发现的Bug数量',
  `bug_fix_status` INT(11) DEFAULT 0 COMMENT 'Bug修复状态（0=未处理，1=修复中，2=已完成）',
  `review_conclusion` TEXT DEFAULT NULL COMMENT '审视结论',
  `img_url` VARCHAR(512) DEFAULT NULL COMMENT '审视截图URL',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  KEY `idx_review_id` (`review_id`),
  KEY `idx_sprint_backlog` (`sprint_backlog_id`),
  CONSTRAINT `fk_review_record_review` FOREIGN KEY (`review_id`) REFERENCES `scrum_review_plans` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_review_record_backlog` FOREIGN KEY (`sprint_backlog_id`) REFERENCES `scrum_sprint_backlogs` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审视记录';

-- 11. 燃尽图快照表（依赖scrum_sprints、scrum_products，均已创建）
DROP TABLE IF EXISTS `scrum_burndown_snapshots`;
CREATE TABLE `scrum_burndown_snapshots` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '快照ID（主键）',
  `sprint_id` BIGINT NOT NULL COMMENT '关联Sprint ID（关联scrum_sprints.id，数值型匹配）',
  `product_id` BIGINT NOT NULL COMMENT '关联产品ID（关联scrum_products.id，数值型匹配）',
  `snapshot_date` DATE NOT NULL COMMENT '快照日期',
  `planned_remaining_workload` DECIMAL(10,2) NOT NULL COMMENT '计划剩余工作量（人日）',
  `actual_remaining_workload` DECIMAL(10,2) NOT NULL COMMENT '实际剩余工作量（人日）',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sprint_snapshotdate` (`sprint_id`, `snapshot_date`) COMMENT '同一Sprint每日仅1条快照',
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_burndown_sprint` FOREIGN KEY (`sprint_id`) REFERENCES `scrum_sprints` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_burndown_product` FOREIGN KEY (`product_id`) REFERENCES `scrum_products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='燃尽图快照表';

-- 12. 用户反馈表（依赖scrum_products、scrum_sprints，均已创建）
DROP TABLE IF EXISTS `scrum_user_feedback`;
CREATE TABLE `scrum_user_feedback` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` BIGINT DEFAULT NULL COMMENT '产品ID（关联scrum_products.id，数值型匹配）',
  `sprint_id` BIGINT DEFAULT NULL COMMENT 'Sprint ID（关联scrum_sprints.id，数值型匹配）',
  `operation_desc` TEXT DEFAULT NULL COMMENT '用户操作描述',
  `problem_solving` TEXT DEFAULT NULL COMMENT '问题解决过程',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  KEY `idx_product_sprint` (`product_id`, `sprint_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户反馈表';

-- 13. 站内信息表（修复sender_id/receiver_id类型，依赖sys_user，已创建）
DROP TABLE IF EXISTS `scrum_sms`;
CREATE TABLE `scrum_sms` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(32) NOT NULL COMMENT '信息标题',
  `sender_id` BIGINT NOT NULL COMMENT '发送者ID（关联sys_user.id，数值型匹配）',
  `receiver_id` BIGINT NOT NULL COMMENT '接收者ID（关联sys_user.id，数值型匹配）',
  `read_state` INT(11) NOT NULL COMMENT '阅读状态（1=未读，2=已读）',
  `content` TEXT DEFAULT NULL COMMENT '信息内容',
  `content_level` INT(11) NOT NULL COMMENT '内容级别（1=普通，2=一般，3=紧急）',
  `content_type` INT(11) NOT NULL COMMENT '内容类型（1=消息，2=通知，3=公告，4=其他）',
  `send_time` DATETIME NOT NULL COMMENT '发送时间',
  `read_time` DATETIME DEFAULT NULL COMMENT '阅读时间',
  `file_url` VARCHAR(500) DEFAULT NULL COMMENT '附件URL',
  `link_url` VARCHAR(500) DEFAULT NULL COMMENT '跳转链接',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` BIGINT DEFAULT NULL COMMENT '创建人（关联sys_user.id，数值型匹配）',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  KEY `idx_sender_receiver` (`sender_id`, `receiver_id`),
  KEY `idx_send_time` (`send_time`),
  CONSTRAINT `fk_sms_sender` FOREIGN KEY (`sender_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_sms_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='站内信息表';


-- ------------------------------
-- 第五批次：附件与动态层（依赖所有基础表）
-- ------------------------------
-- 1. 附件表（修复uploader_id类型，依赖sys_user，已创建）
DROP TABLE IF EXISTS `sys_attachments`;
CREATE TABLE `sys_attachments` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '附件ID（主键）',
  `attach_type` ENUM('product', 'sprint', 'product_backlog', 'sprint_backlog', 'meeting', 'review') NOT NULL COMMENT '关联模块：product=产品，sprint=Sprint，product_backlog=产品待办，sprint_backlog=Sprint待办，meeting=会议，review=审视',
  `attach_id` BIGINT NOT NULL COMMENT '关联模块ID（如scrum_products.id、scrum_sprints.id，数值型匹配）',
  `attachment_name` VARCHAR(50) NOT NULL COMMENT '附件名称',
  `file_type` VARCHAR(50) DEFAULT NULL COMMENT '附件类型（如pdf、doc、jpg）',
  `file_path` TEXT DEFAULT NULL COMMENT '附件路径（如/opt/attach/2025/xxx.pdf）',
  `file_size` BIGINT DEFAULT 0 COMMENT '文件大小（字节）',
  `uploader_id` BIGINT DEFAULT NULL COMMENT '上传人ID（关联sys_user.id，数值型匹配）',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updater` BIGINT DEFAULT NULL COMMENT '修改人ID（关联sys_user.id，数值型匹配）',
  PRIMARY KEY (`id`),
  KEY `idx_attach_type_id` (`attach_type`, `attach_id`),
  KEY `idx_uploader` (`uploader_id`),
  CONSTRAINT `fk_attachment_uploader` FOREIGN KEY (`uploader_id`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='附件信息表';

-- 2. 系统动态表（依赖sys_user，已创建）
DROP TABLE IF EXISTS `sys_activities`;
CREATE TABLE `sys_activities` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '动态ID（主键）',
  `user_id` BIGINT NOT NULL COMMENT '操作用户ID（关联sys_user.id，数值型匹配）',
  `user_name` VARCHAR(100) NOT NULL COMMENT '用户名（冗余，便于前端显示）',
  `action_type` VARCHAR(50) NOT NULL COMMENT '操作类型（add=添加，delete=删除，update=修改，view=查看）',
  `target_type` VARCHAR(50) NOT NULL COMMENT '操作对象类型（product=产品，sprint=Sprint，sprint_backlog=Sprint待办）',
  `target_id` BIGINT NOT NULL COMMENT '操作对象ID（数值型匹配关联表主键）',
  `activity_content` VARCHAR(512) NOT NULL COMMENT '动态内容（如“Rick添加Sprint待办：9.23考试”“李华删除产品：XX系统”）',
  `create_time` DATETIME NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target_type_id` (`target_type`, `target_id`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_activity_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统动态表（全局操作记录）';


-- ------------------------------
-- 收尾：创建视图 + 恢复外键检查
-- ------------------------------
-- 1. 用户视图（依赖sys_user、sys_user_role、sys_role、sys_role_dept、sys_dept，均已创建）
DROP VIEW IF EXISTS `v_sys_users`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER 
VIEW `v_sys_users` AS 
SELECT 
  d.`dept_name` AS dept_name,
  d.`id` AS dept_id,
  d.`dept_code` AS dept_code,
  r.`role_name` AS role_name,
  r.`role_code` AS role_code,
  u.`id` AS user_id,
  u.`real_name` AS user_name,
  u.`phone` AS mobile,
  u.`sex` AS sex,
  u.`status` AS work_status,
  u.`is_disable` AS account_status
FROM 
  `sys_user` u
LEFT JOIN `sys_user_role` ur ON u.`id` = ur.`user_id`
LEFT JOIN `sys_role` r ON ur.`role_id` = r.`id`
LEFT JOIN `sys_role_dept` rd ON r.`id` = rd.`role_id`
LEFT JOIN `sys_dept` d ON rd.`dept_id` = d.`id`
WHERE 
  u.`is_disable` = 2;

-- 2. 恢复外键检查（确保后续数据操作时外键约束生效）
SET FOREIGN_KEY_CHECKS = 1;