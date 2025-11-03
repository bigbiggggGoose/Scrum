/**
 * Sprint待办事项表 常量
 *
 * @Author:    system
 * @Date:      2025-01-01 00:00:00
 * @Copyright  system
 */

export const SPRINT_BACKLOG_STATUS_ENUM = {
  NOT_STARTED: {
    value: '1',
    desc: '未开始',
  },
  IN_PROGRESS: {
    value: '2',
    desc: '进行中',
  },
  COMPLETED: {
    value: '3',
    desc: '已完成',
  },
};

export const SPRINT_BACKLOG_STATUS_OPTIONS = [
  {
    label: '未开始',
    value: '1',
  },
  {
    label: '进行中',
    value: '2',
  },
  {
    label: '已完成',
    value: '3',
  },
];

export const SPRINT_BACKLOG_PRIORITY_ENUM = {
  HIGH: {
    value: 1,
    desc: '高',
  },
  MEDIUM: {
    value: 2,
    desc: '中',
  },
  LOW: {
    value: 3,
    desc: '低',
  },
};

export const SPRINT_BACKLOG_PRIORITY_OPTIONS = [
  {
    label: '高',
    value: 1,
  },
  {
    label: '中',
    value: 2,
  },
  {
    label: '低',
    value: 3,
  },
];