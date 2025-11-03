<!--
  * 开发记录表（静态演示版）
  * 布局与筛选交互风格保持与 Sprint 看板一致
-->
<template>
  <div class="page-wrap">
    <a-card size="small" class="top-filter">
      <a-row :gutter="[12, 12]" align="middle">
        <a-col>
          <a-checkbox v-model:checked="showPerf">查看绩效员</a-checkbox>
        </a-col>
        <a-col>
          <span class="label">产品</span>
          <a-select v-model:value="currentProductId" style="width:220px" :options="productOptions" @change="onProductChange" placeholder="请选择产品" />
        </a-col>
        <a-col>
          <span class="label">Sprint</span>
          <a-select v-model:value="currentSprintId" style="width:220px" :options="sprintOptions" @change="onSprintChange" placeholder="请选择Sprint" />
        </a-col>
        <a-col>
          <a-button type="primary" @click="onSearch">搜索</a-button>
          <a-button style="margin-left:8px;" @click="onReset">清空</a-button>
        </a-col>
      </a-row>
    </a-card>

    <a-card size="small">
      <a-table
        size="small"
        :dataSource="records"
        :columns="columns"
        rowKey="id"
        bordered
        :pagination="pagination"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'hours'">
            <span>{{ record.hoursText }}</span>
          </template>
          <template v-else-if="column.key === 'progress'">
            <span>{{ record.progress }}%</span>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button size="small">详情</a-button>
              <a-button size="small">编辑</a-button>
              <a-button size="small" danger>删除</a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';

// 顶部筛选静态数据（与 sprint-dashboard 保持一致）
const productOptions = ref([
  { value: 'P-223C-0001', label: 'SmartAdmin 平台' },
  { value: 'P-223C-0002', label: '数据中台' },
]);
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
const sprintBacklogMap = {
  'SM-001': ['登录界面优化', '权限模块重构'],
  'SM-002': ['菜单管理实现', '系统配置接口'],
  'SM-003': ['登录界面优化', '菜单管理实现', '系统配置接口'],
  'SM-004': ['权限模块重构'],
  'DC-001': ['登录界面优化', '系统配置接口'],
  'DC-002': ['权限模块重构', '菜单管理实现'],
  'DC-003': ['系统配置接口', '权限模块重构', '登录界面优化'],
  'DC-004': ['菜单管理实现'],
};

const sprintOptions = computed(() => productSprintMap[currentProductId.value] || []);
const currentProductId = ref('P-223C-0001');
const currentSprintId = ref(productSprintMap[currentProductId.value]?.[0]?.value || null);
const showPerf = ref(false);

const teamPool = ['开发成员A', '开发成员B', '开发成员C'];
function pickMember(){ return teamPool[Math.floor(Math.random()*teamPool.length)]; }
function pickHours(){ return Math.random() < 0.5 ? '3 / 3h' : '8 / 8h'; }

// 表格列
const columns = [
  { title: '完成时间', dataIndex: 'finishTime', width: 180, key: 'finishTime' },
  { title: '团队成员', dataIndex: 'member', width: 120, key: 'member' },
  { title: '执行任务', dataIndex: 'task', ellipsis: true, key: 'task' },
  { title: '累计消耗工时', dataIndex: 'hoursText', width: 120, key: 'hours' },
  { title: '开发进度', dataIndex: 'progress', width: 100, key: 'progress' },
  { title: '日志数', dataIndex: 'logCount', width: 80, key: 'logCount' },
  { title: '操作', dataIndex: 'action', width: 180, fixed: 'right', key: 'action' },
];

const records = ref([]);
const pagination = ref({ pageSize: 15, current: 1, showSizeChanger: false });

function genRecordsBySprint(){
  const titles = sprintBacklogMap[currentSprintId.value] || [];
  const now = Date.now();
  const rows = [];
  // 每个待办生成 2 条开发记录，模拟日志
  titles.forEach((t, i) => {
    for(let k=0;k<2;k++){
      const dt = new Date(now - (i*86400000 + k*3600000));
      rows.push({
        id: `${currentSprintId.value}-${i}-${k}`,
        finishTime: `${dt.getFullYear()}-${String(dt.getMonth()+1).padStart(2,'0')}-${String(dt.getDate()).padStart(2,'0')} ${String(dt.getHours()).padStart(2,'0')}:${String(dt.getMinutes()).padStart(2,'0')}:${String(dt.getSeconds()).padStart(2,'0')}`,
        member: pickMember(),
        task: t,
        hoursText: pickHours(),
        progress: 100,
        logCount: 1,
      });
    }
  });
  records.value = rows;
}

function onProductChange(){
  const opts = productSprintMap[currentProductId.value] || [];
  currentSprintId.value = opts.length ? opts[0].value : null;
  genRecordsBySprint();
}
function onSprintChange(){ genRecordsBySprint(); }
function onSearch(){ genRecordsBySprint(); }
function onReset(){ currentProductId.value = 'P-223C-0001'; onProductChange(); }

onMounted(() => { genRecordsBySprint(); });
watch([currentProductId, currentSprintId], () => { /* 保留联动一致性 */ });
</script>

<style scoped>
.page-wrap{ padding: 8px 0; }
.top-filter{ margin: 0 0 8px 0; }
.label{ color:#666; margin-right:6px; }
</style>