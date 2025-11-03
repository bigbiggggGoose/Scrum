/*
 * Scrum 角色与演示路由（用于预览/静态演示）
 */
import SmartLayout from '/@/layout/index.vue';

export const scrumRouters = [
  {
    path: '/scrum',
    name: '_scrum',
    component: SmartLayout,
    meta: {
      title: 'Scrum',
      hideInMenu: true,
    },
    children: [
      {
        path: '/scrum/role',
        name: 'ScrumRole',
        component: () => import('/@/views/scrum/index.vue'),
        meta: {
          title: '角色管理（Scrum）',
          hideInMenu: true,
        },
      },
      // Sprint 看板（静态演示）
      {
        path: '/scrum/sprints/dashboard',
        name: 'ScrumSprintsDashboard',
        component: () => import('/@/views/business/scrum-sprints/sprint-dashboard.vue'),
        meta: { title: 'Sprint 看板（演示）', hideInMenu: false },
      },
      // Sprint Backlog（示例）
      {
        path: '/scrum/sprint-backlog/list',
        name: 'ScrumSprintBacklogList',
        component: () => import('/@/views/business/scrum-sprint-backlog/index.vue'),
        meta: { title: 'Sprint Backlog', hideInMenu: false },
      },
      // 产品模块（示例）
      {
        path: '/scrum/products/list',
        name: 'ScrumProductsList',
        component: () => import('/@/views/business/scrum-products/scrum-products-list.vue'),
        meta: { title: '产品管理', hideInMenu: false },
      },
      // 开发记录表（静态演示）
      {
        path: '/scrum/dev-records/list',
        name: 'ScrumDevRecordsList',
        component: () => import('/@/views/business/scrum-dev-records/dev-records-list.vue'),
        meta: { title: '开发记录表（演示）', hideInMenu: false },
      },
      // 会议功能（静态演示）
      {
        path: '/scrum/meetings/list',
        name: 'ScrumMeetingsList',
        component: () => import('/@/views/business/scrum-meetings/meetings-list.vue'),
        meta: { title: '会议（演示）', hideInMenu: false },
      },
      // 看板模块（静态演示）
      {
        path: '/scrum/kanban/board',
        name: 'ScrumKanbanBoard',
        component: () => import('/@/views/business/scrum-board/kanban-board.vue'),
        meta: { title: '看板模块（演示）', hideInMenu: false },
      },
    ],
  },
];