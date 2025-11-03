<!--
  * Sprint看板（静态演示版）
  * 仅前端：不依赖后端接口，使用内置静态数据模拟
-->
<template>
  <div class="sprint-dashboard">
    <!-- 顶部筛选（独立卡片） -->
    <a-card class="smart-margin-top10" size="small">
      <div class="top-bar">
        <div class="selector">
          <span class="label">产品</span>
          <a-select v-model:value="currentProductId" style="width:220px" :options="productOptions" @change="onProductChange" placeholder="请选择产品" />
        </div>
        <div class="selector">
          <span class="label">Sprint</span>
          <a-select v-model:value="currentSprintId" style="width:220px" :options="sprintOptions" @change="onSprintChange" placeholder="请选择Sprint" />
        </div>

      </div>
    </a-card>

    <!-- 顶部指标（不叠加，独立一行卡片） -->
    <a-card class="smart-margin-top10" size="small">
      <a-row :gutter="[12, 12]" class="metrics-row">
        <a-col :span="6">
          <a-card size="small" class="metric-card">
            <div class="metric-title">正在进行的Sprint数量</div>
            <div class="metric-value">{{ summary.inProgress }}/13</div>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card size="small" class="metric-card">
            <div class="metric-title">会议数量</div>
            <div class="metric-value">{{ summary.meetingCount }}</div>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card size="small" class="metric-card">
            <div class="metric-title">审批bug数量</div>
            <div class="metric-value">{{ summary.approveBugCount }}</div>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card size="small" class="metric-card">
            <div class="metric-title">可用待办工时</div>
            <div class="metric-value">{{ summary.availableHours }} 小时</div>
          </a-card>
        </a-col>
      </a-row>
    </a-card>

    <!-- 主体：左中右布局 -->
    <div class="main-body smart-margin-top10">
      <!-- 左侧：标签页 + 待办网格 -->
      <div class="left-pane">
        <a-card class="smart-margin-top10" size="small">
          <a-tabs v-model:activeKey="activeTab">
            <a-tab-pane key="todo" tab="待办">
              <div class="todo-grid">
                <template v-for="(item, idx) in todoItems" :key="idx">
                  <div class="todo-card" :class="{ done: item.done }">
                    <div class="todo-title">{{ item.title }}</div>
                    <div class="todo-sub">团队成员：{{ item.member }}</div>
                    <a-tag class="todo-status" :color="item.done ? 'success' : 'processing'">{{ item.done ? '已完成' : '进行中' }}</a-tag>
                    <div class="todo-bottom">
                      <div>完成进度：{{ item.progress }}%</div>
                      <div>计划周期：{{ item.period }}</div>
                    </div>
                  </div>
                </template>
              </div>
            </a-tab-pane>
            <a-tab-pane key="approve" tab="审批">
              <a-empty description="演示：审批数据暂未接入" />
            </a-tab-pane>
            <a-tab-pane key="meeting" tab="会议">
              <a-empty description="演示：会议数据暂未接入" />
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </div>

      <!-- 右侧：燃尽图 + 动态 -->
      <div class="right-pane">
        <a-card class="smart-margin-top10" size="small">
          <div ref="chartRef" style="height:300px;" />
        </a-card>
        
        <!-- 删除：动态列表卡片 -->
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, reactive, watch, computed } from 'vue';
import * as echarts from 'echarts';

// ---------------- 静态演示数据 ----------------
const productOptions = ref([
  { value: 'P-223C-0001', label: 'SmartAdmin 平台' },
  { value: 'P-223C-0002', label: '数据中台' },
]);

// 产品→Sprint映射（静态演示）
const productSprintMap = {
  'P-223C-0001': [
    { value: 'SM-001', label: '报表统计模块' },
    { value: 'SM-002', label: '系统设置模块' },
    { value: 'SM-003', label: '用户管理模块' },
    { value: 'SM-004', label: '基础架构搭建' },
  ],
  'P-223C-0002': [
    { value: 'DC-001', label: '可视化与监控' },
    { value: 'DC-002', label: '数据服务与API' },
    { value: 'DC-003', label: '数据存储与治理' },
    { value: 'DC-004', label: '数据采集层开发' },
  ],
};

// Sprint 下拉选项依据当前产品动态计算
const sprintOptions = computed(() => productSprintMap[currentProductId.value] || []);
const moduleOptions = ref([
  { value: '系统设计', label: '系统设计' },
  { value: '系统设置模块', label: '系统设置模块' },
]);

const currentProductId = ref('P-223C-0001');
// 定义当前 Sprint（基于当前产品的首个选项）
const currentSprintId = ref(productSprintMap[currentProductId.value]?.[0]?.value || null);
// 供演示的 Sprint→待办名称映射（与截图一致的四项）
const sprintBacklogMap = {
  // SmartAdmin 平台
  'SM-001': ['登录界面优化', '权限模块重构'],
  'SM-002': ['菜单管理实现', '系统配置接口'],
  'SM-003': ['登录界面优化', '菜单管理实现', '系统配置接口'],
  'SM-004': ['权限模块重构'],
  // 数据中台
  'DC-001': ['登录界面优化', '系统配置接口'],
  'DC-002': ['权限模块重构', '菜单管理实现'],
  'DC-003': ['系统配置接口', '权限模块重构', '登录界面优化'],
  'DC-004': ['菜单管理实现'],
};

const teamPool = ['开发成员A', '开发成员B', '开发成员C'];
function pickMember() {
  const i = Math.floor(Math.random() * teamPool.length);
  return teamPool[i];
}
function randProgress() {
  const v = Math.floor(Math.random() * 60) + 40; // 40-100
  return Math.min(v, 100);
}

function updateTodoBySprint() {
  const titles = sprintBacklogMap[currentSprintId.value] || [];
  todoItems.value = titles.map((t) => ({
    title: t,
    member: pickMember(),
    done: Math.random() > 0.5,
    progress: randProgress(),
    period: '2025-10-26 — 2025-11-15',
  }));
}
const moduleName = ref('系统设计');

const summary = reactive({
  inProgress: 2,
  approvalCount: 0,
  teamName: 'Team223B',
  meetingCount: 0,
  approveBugCount: 0,
  availableHours: 386,
});

const activeTab = ref('todo');

const todoItems = ref([
  { title: '启动项目，分析项目内容', member: '林健安', done: true, progress: 100, period: '2025-09-27 —' },
  { title: '了解系统层型，查看版本相关', member: '崔豪峰', done: true, progress: 100, period: '2025-09-27 —' },
  { title: '学习scrum敏捷知识', member: '何家伟', done: true, progress: 100, period: '2025-09-25 —' },
  { title: '学习理论知识', member: '陈文忠', done: true, progress: 100, period: '2025-09-24 —' },
  { title: '学习了解项目前端代码', member: '梁攀峰', done: true, progress: 100, period: '2025-09-24 —' },
  { title: '完成系统原型(ER建模)', member: '黄超', done: true, progress: 100, period: '2025-09-24 —' },
]);

// 删除 activities 定义
// const activities = ref([
//   { title: '2025-09-29 23:09 林健安 工作日志 完成Sprint待办作业-数据库', desc: '演示动态内容1' },
//   { title: '2025-09-29 09:30 梁攀峰 添加Sprint待办-准备表/数据库', desc: '演示动态内容2' },
// ]);

// ---------------- 行为 ----------------
function onProductChange() {
  // 演示：按产品切换联动信息
  summary.teamName = currentProductId.value === 'P-223C-0001' ? 'Team223B' : 'Team223A';
  summary.availableHours = currentProductId.value === 'P-223C-0001' ? 386 : 360;
  // 重置当前 Sprint 为该产品的第一个选项
  const opts = productSprintMap[currentProductId.value] || [];
  currentSprintId.value = opts.length ? opts[0].value : null;
  updateTodoBySprint();
}
function onSprintChange() {
  // Sprint 切换后，重生成待办列表，指标由 todoItems 监听统一更新
  updateTodoBySprint();
}

// ---------------- 燃尽图 ----------------
const chartRef = ref();
let chart;
// 产品维度的燃尽图数据（静态演示映射）
const chartDataByProduct = {
  'P-223C-0001': {
    x: ['10-26', '10-27', '10-28', '10-29', '10-30', '11-01'],
    ideal: [400, 360, 320, 280, 240, 200],
    actual: [400, 382, 350, 328, 305, 288],
  },
  'P-223C-0002': {
    x: ['10-26', '10-27', '10-28', '10-29', '10-30', '11-01'],
    ideal: [360, 320, 280, 240, 200, 160],
    actual: [360, 345, 330, 300, 275, 255],
  },
};
// （可选）Sprint维度覆盖：若存在对应Sprint数据则优先使用
const chartDataBySprint = {
  'SM-002': { x: ['10-26','10-27','10-28','10-29','10-30','11-01'], ideal: [300,270,240,210,180,150], actual: [300,285,260,235,210,190] },
  'DC-004': { x: ['10-26','10-27','10-28','10-29','10-30','11-01'], ideal: [280,250,220,190,160,130], actual: [280,268,250,226,205,190] },
};
function renderChart() {
  if (!chartRef.value) return;
  const inst = chart || echarts.init(chartRef.value);
  chart = inst;
  // 先取Sprint映射，没有则取产品映射，最后取默认演示
  const pd = chartDataBySprint[currentSprintId.value] || chartDataByProduct[currentProductId.value] || {
    x: ['09-25', '09-26', '09-27', '09-28', '09-29', '09-30'],
    ideal: [500, 450, 400, 350, 300, 250],
    actual: [500, 480, 450, 430, 420, 410],
  };
  const opt = {
    tooltip: { trigger: 'axis' },
    legend: { data: ['理想燃尽', '实际燃尽'] },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: pd.x },
    yAxis: { type: 'value' },
    series: [
      { name: '理想燃尽', type: 'line', data: pd.ideal, smooth: true, color: '#2f7ed8' },
      { name: '实际燃尽', type: 'line', data: pd.actual, smooth: true, color: '#d62728' },
    ],
  };
  chart.setOption(opt, true);
}

onMounted(() => {
  renderChart();
  // 初始化根据当前 Sprint 生成待办
  updateTodoBySprint();
});

watch([currentProductId, currentSprintId], () => {
  renderChart();
});
// 独立监听 Sprint 变化生成待办（保证直接修改值时也更新）
watch(currentSprintId, () => {
  updateTodoBySprint();
});
// 新增：监听待办列表变化，实时同步顶部“正在进行的Sprint数量”
watch(todoItems, (list) => {
  summary.inProgress = (list || []).length;
});
</script>

<style scoped>
.sprint-dashboard { padding: 10px; }
.top-bar { display: flex; gap: 16px; align-items: center; flex-wrap: wrap; }
.selector { display: flex; align-items: center; gap: 10px; }
.label { color:#666; }

.metrics-row { margin-top: 8px; }
.metric-card :deep(.ant-card-body) { padding: 10px; }
.metric-title { color:#666; font-size: 13px; }
.metric-value { font-size: 18px; font-weight: 600; color:#222; }

.main-body { display: grid; grid-template-columns: 2fr 1fr; gap: 12px; }
.todo-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; }
.todo-card { background: #eaf7e9; border: 1px solid #c6e6c4; border-radius: 4px; padding: 10px; }
.todo-card.done { background: #b6ddb3; }
.todo-title { font-weight: 600; margin-bottom: 6px; }
.todo-sub { color:#666; }
.todo-status { margin: 6px 0; }
.todo-bottom { display:flex; justify-content: space-between; color:#333; }
</style>