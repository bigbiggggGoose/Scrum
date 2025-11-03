START TRANSACTION;

-- 顶级角色：超级管理员（role_code=admin）
INSERT INTO `sys_role` (`role_code`,`role_name`,`parent_id`,`order_num`,`level`,`status`,`remark`)
SELECT 'admin','超级管理员',0,1,1,1,''
WHERE NOT EXISTS (SELECT 1 FROM `sys_role` WHERE `role_code`='admin');

-- 取超级管理员ID
SET @admin_role_id := (SELECT `id` FROM `sys_role` WHERE `role_code`='admin' LIMIT 1);

-- 子角色：项目经理（role_code=mgr）
INSERT INTO `sys_role` (`role_code`,`role_name`,`parent_id`,`order_num`,`level`,`status`,`remark`)
SELECT 'mgr','项目经理',COALESCE(@admin_role_id,0),2,2,1,''
WHERE NOT EXISTS (SELECT 1 FROM `sys_role` WHERE `role_code`='mgr');

-- 子角色：开发实习生（role_code=dev）
INSERT INTO `sys_role` (`role_code`,`role_name`,`parent_id`,`order_num`,`level`,`status`,`remark`)
SELECT 'dev','开发实习生',COALESCE(@admin_role_id,0),3,2,1,''
WHERE NOT EXISTS (SELECT 1 FROM `sys_role` WHERE `role_code`='dev');

-- 子角色：高校实习生1（role_code=dev2）
INSERT INTO `sys_role` (`role_code`,`role_name`,`parent_id`,`order_num`,`level`,`status`,`remark`)
SELECT 'dev2','高校实习生1',COALESCE(@admin_role_id,0),3,2,1,''
WHERE NOT EXISTS (SELECT 1 FROM `sys_role` WHERE `role_code`='dev2');

-- 子角色：高校实习生2（role_code=dev3）
INSERT INTO `sys_role` (`role_code`,`role_name`,`parent_id`,`order_num`,`level`,`status`,`remark`)
SELECT 'dev3','高校实习生2',COALESCE(@admin_role_id,0),3,2,1,''
WHERE NOT EXISTS (SELECT 1 FROM `sys_role` WHERE `role_code`='dev3');

-- 额外角色：master（展示名称为 admin，便于对照截图）
INSERT INTO `sys_role` (`role_code`,`role_name`,`parent_id`,`order_num`,`level`,`status`,`remark`)
SELECT 'master','admin',0,10,1,1,''
WHERE NOT EXISTS (SELECT 1 FROM `sys_role` WHERE `role_code`='master');

COMMIT;