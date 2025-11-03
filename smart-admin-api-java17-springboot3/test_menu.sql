-- 测试菜单数据
SELECT 
    m.menu_id,
    m.menu_name,
    m.parent_id,
    m.sort,
    m.visible_flag,
    m.deleted_flag,
    CASE 
        WHEN rm.role_id IS NOT NULL THEN '有权限'
        ELSE '无权限'
    END as permission_status
FROM t_menu m
LEFT JOIN t_role_menu rm ON m.menu_id = rm.menu_id AND rm.role_id = 1
WHERE m.menu_id >= 1000
ORDER BY m.menu_id;
