<!--
  * 看板模块（按产品驱动：四个待办、燃尽图联动、团队文案）
  * 仅前端：不依赖后端接口，使用内置静态数据模拟
-->
<template>
  <div class="sprint-dashboard">
    <!-- 顶部筛选：仅产品 -->
    <a-card class="smart-margin-top10" size="small">
      <div class="top-bar">
        <div class="selector">
          <span class="label">产品</span>
          <a-select v-model:value="currentProductId" style="width:220px" :options="productOptions" @change="onProductChange" placeholder="请选择产品" />
        </div>
      </div>
    </a-card>

    <!-- 顶部指标（对齐样式） -->
    <a-card class="smart-margin-top10" size="small">
      <a-row :gutter="[12, 12]" class="metrics-row">
        <a-col :span="6">
          <a-card size="small" class="metric-card">
            <div class="metric-title">正在进行的Sprint数量</div>
            <div class="metric-value">{{ summary.inProgress }}</div>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card size="small" class="metric-card">
            <div class="metric-title">开发团队</div>
            <div class="metric-value">{{ teamLabel }}</div>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card size="small" class="metric-card">
            <div class="metric-title">产品经理</div>
            <div class="metric-value">{{ summary.productManager }}</div>
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
      <!-- 左侧：标签页 + 待办网格（随产品变更） -->
      <div class="left-pane">
        <a-card class="smart-margin-top10" size="small">
          <a-tabs v-model:activeKey="activeTab">
            <a-tab-pane key="todo" tab="待办">
              <div class="todo-grid">
                <template v-for="(item, idx) in todoItems" :key="idx">
                  <div class="todo-card" :class="{ done: item.done }">
                    <div class="todo-title">{{ item.title }}</div>
                    <div class="todo-sub">开发团队：{{ item.team || teamLabel }}</div>
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

      <!-- 右侧：燃尽图（按产品切换） -->
      <div class="right-pane">
        <a-card class="smart-margin-top10" size="small">
          <div ref="chartRef" style="height:300px;" />
        </a-card>
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

// 产品→待办映射：每个产品 4 项，内容与示例图一致
const productBacklogMap = {
  'P-223C-0001': ['登录界面优化', '权限模块重构', '菜单管理实现', '系统配置接口'],
  'P-223C-0002': ['可视化与监控', '数据服务与API', '数据存储与治理', '数据采集层开发'],
};

// 产品→开发团队：数据中台→团队A，SmartAdmin→团队B
const teamLabel = computed(() => (currentProductId.value === 'P-223C-0002' ? '团队A' : '团队B'));

const currentProductId = ref('P-223C-0001');

function randProgress() {
  const v = Math.floor(Math.random() * 60) + 40; // 40-100
  return Math.min(v, 100);
}

function updateTodoByProduct() {
  const titles = productBacklogMap[currentProductId.value] || [];
  todoItems.value = titles.map((t) => ({
    title: t,
    team: teamLabel.value,
    done: Math.random() > 0.5,
    progress: randProgress(),
    period: '2025-10-26 — 2025-11-15',
  }));
}

const summary = reactive({
  inProgress: 2,
  approvalCount: 0,
  teamName: '团队B',
  meetingCount: 0,
  approveBugCount: 0,
  availableHours: 386,
  productManager: 'Jack',
});

const activeTab = ref('todo');

const todoItems = ref([]);

// ---------------- 行为 ----------------
function onProductChange() {
  // 联动指标与团队
  summary.teamName = currentProductId.value === 'P-223C-0001' ? '团队B' : '团队A';
  summary.availableHours = currentProductId.value === 'P-223C-0001' ? 386 : 360;
  updateTodoByProduct();
  renderChart();
}

// ---------------- 燃尽图（按产品） ----------------
const chartRef = ref();
let chart;
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
function renderChart() {
  if (!chartRef.value) return;
  const inst = chart || echarts.init(chartRef.value);
  chart = inst;
  const pd = chartDataByProduct[currentProductId.value] || {
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
  updateTodoByProduct();
  renderChart();
});

watch(currentProductId, () => {
  updateTodoByProduct();
  renderChart();
});
// 同步顶部“正在进行的Sprint数量”
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
.todo-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 8px; }
.todo-card { background: #eaf7e9; border: 1px solid #c6e6c4; border-radius: 4px; padding: 10px; }
.todo-card.done { background: #b6ddb3; }
.todo-title { font-weight: 600; margin-bottom: 6px; }
.todo-sub { color:#666; }
.todo-status { margin: 6px 0; }
.todo-bottom { display:flex; justify-content: space-between; color:#333; }
</style>