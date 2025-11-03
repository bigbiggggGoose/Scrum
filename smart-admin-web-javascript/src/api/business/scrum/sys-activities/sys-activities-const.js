/**
 * 系统动态表（全局操作记录） 常量
 *
 * @Author:    a
 * @Date:      2025-09-30 10:32:40
 * @Copyright  a
 */

// 操作类型枚举
export const ACTION_TYPE_ENUM = {
  ADD: 'add',
  DELETE: 'delete', 
  UPDATE: 'update',
  VIEW: 'view'
};

// 操作对象类型枚举
export const TARGET_TYPE_ENUM = {
  PRODUCT: 'product',
  SPRINT: 'sprint',
  SPRINT_BACKLOG: 'sprint_backlog'
};
