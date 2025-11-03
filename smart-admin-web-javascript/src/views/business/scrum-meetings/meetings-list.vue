<!--
  * 会议功能（静态演示版）
  * 布局与交互保持与已实现的模块一致：顶部筛选 + 表格
-->
<template>
  <div class="page-wrap">
    <!-- 顶部筛选条 -->
    <a-card size="small" class="top-filter">
      <a-row :gutter="[12, 12]" align="middle">
        <a-col>
          <span class="label">产品</span>
          <a-select v-model:value="currentProductId" style="width:220px" :options="productOptions" @change="onProductChange" placeholder="请选择产品" />
        </a-col>
        <a-col>
          <span class="label">迭代</span>
          <a-select v-model:value="currentSprintId" style="width:220px" :options="sprintOptions" @change="onSprintChange" placeholder="请选择Sprint" />
        </a-col>
        <a-col>
          <span class="label">团队</span>
          <a-tag>{{ teamName }}</a-tag>
        </a-col>
        <a-col>
          <a-button type="primary">添加</a-button>
          <a-button style="margin-left:8px;">批量删除</a-button>
        </a-col>
      </a-row>
    </a-card>

    <!-- 表格 -->
    <a-card size="small">
      <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="id"
        bordered
        :pagination="pagination"
        :scroll="{ x: 1200, y: 600 }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'participants'">
            <span>{{ record.participants.join('、') }}</span>
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

// 顶部筛选静态数据（与 sprint 看板一致）
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
const sprintOptions = computed(() => productSprintMap[currentProductId.value] || []);
const currentProductId = ref('P-223C-0001');
const currentSprintId = ref(productSprintMap[currentProductId.value]?.[0]?.value || null);
const teamName = ref('Team223B');

// 静态会议名称映射（每个 Sprint 的会议列表）
const meetingNamesBySprint = {
  'SM-001': ['每日站会', 'Sprint计划会', 'Sprint评审会', 'Sprint回顾会'],
  'SM-002': ['每日站会', '技术评审会', '需求澄清会'],
  'SM-003': ['每日站会', '版本发布评审会'],
  'SM-004': ['每日站会', '架构讨论会'],
  'DC-001': ['每日站会', '数据治理评审会'],
  'DC-002': ['每日站会', 'API方案评审会'],
  'DC-003': ['每日站会', '存储方案评审会'],
  'DC-004': ['每日站会', '采集方案评审会'],
};

const meetingTypePool = ['站会', '评审会', '回顾会', '计划会', '讨论会'];
const hostPool = ['ScrumMaster', 'ProductOwner'];
const teamPool = ['开发成员A', '开发成员B', '开发成员C'];
function pick(arr){ return arr[Math.floor(Math.random()*arr.length)]; }
function pickParticipants(){
  const shuffled = [...teamPool].sort(() => Math.random() - 0.5);
  return shuffled.slice(0, Math.floor(Math.random()*teamPool.length) + 1);
}
function formatDate(dt){
  const y = dt.getFullYear();
  const m = String(dt.getMonth()+1).padStart(2,'0');
  const d = String(dt.getDate()).padStart(2,'0');
  const hh = String(dt.getHours()).padStart(2,'0');
  const mm = String(dt.getMinutes()).padStart(2,'0');
  const ss = String(dt.getSeconds()).padStart(2,'0');
  return `${y}-${m}-${d} ${hh}:${mm}:${ss}`;
}

// 表格列
const columns = [
  { title: '会议名称', dataIndex: 'meetingName', width: 220, key: 'meetingName' },
  { title: '审批计划名称', dataIndex: 'approvePlan', width: 220, key: 'approvePlan' },
  { title: '会议时间', dataIndex: 'meetingTime', width: 180, key: 'meetingTime' },
  { title: '会议类型', dataIndex: 'meetingType', width: 120, key: 'meetingType' },
  { title: '主持人', dataIndex: 'host', width: 120, key: 'host' },
  { title: '参会人', dataIndex: 'participants', width: 220, key: 'participants' },
  { title: '操作', dataIndex: 'action', width: 180, fixed: 'right', key: 'action' },
];

const tableData = ref([]);
const pagination = ref({ pageSize: 15, current: 1, showSizeChanger: false });

function genMeetings(){
  const names = meetingNamesBySprint[currentSprintId.value] || [];
  const now = Date.now();
  tableData.value = names.map((n, i) => {
    const dt = new Date(now - i*86400000);
    return {
      id: `${currentSprintId.value}-${i}`,
      meetingName: n,
      approvePlan: `${resolveSprintLabel(currentSprintId.value)}评审计划`,
      meetingTime: formatDate(dt),
      meetingType: pick(meetingTypePool),
      host: pick(hostPool),
      participants: pickParticipants(),
    };
  });
}

function resolveSprintLabel(sid){
  const opts = sprintOptions.value || [];
  return (opts.find(o => o.value === sid)?.label) || sid;
}

function onProductChange(){
  // 切换产品时：更新团队名称、重置 Sprint 并刷新表格
  teamName.value = currentProductId.value === 'P-223C-0001' ? 'Team223B' : 'Team223A';
  const opts = productSprintMap[currentProductId.value] || [];
  currentSprintId.value = opts.length ? opts[0].value : null;
  genMeetings();
}
function onSprintChange(){ genMeetings(); }

onMounted(() => { genMeetings(); });
watch([currentProductId, currentSprintId], () => { /* 保持联动一致性 */ });
</script>

<style scoped>
.page-wrap{ padding: 8px 0; }
.top-filter{ margin: 0 0 8px 0; }
.label{ color:#666; margin-right:6px; }
</style>