<template>
  <div class="page-wrap">
    <!-- 顶部筛选条（与右图一致的横向布局） -->
    <a-space align="center" wrap class="top-filter">
      <span class="label">产品</span>
      <a-select v-model:value="currentProductId" style="width:220px" placeholder="选择产品" allow-clear @dropdownVisibleChange="loadProducts" @change="onProductChange">
        <a-select-option v-for="p in productOptions" :key="p.id" :value="p.id">{{ p.name }}</a-select-option>
      </a-select>

      <span class="label">Sprint</span>
      <a-select v-model:value="currentSprintId" style="width:240px" placeholder="选择迭代" allow-clear @dropdownVisibleChange="loadSprints" :disabled="!currentProductId" @change="onSprintChange">
        <a-select-option v-for="s in sprintOptions" :key="s.id" :value="s.id">{{ s.name }}</a-select-option>
      </a-select>

      <span class="stat">人日：{{ manDayUsed }} / {{ manDayTotal }}</span>
      <span class="stat">Team：{{ teamName || '—' }}</span>
      <span class="stat">工时</span>

      <a-button type="primary" @click="applySelection" class="smart-margin-left10">加载待办</a-button>
      <a-button @click="openCreate" class="smart-margin-left10">+ 添加执行</a-button>
    </a-space>

    <!-- 统计与提示条（右图的灰字与蓝字区域） -->
    <div class="summary-line">
      <span class="done">已完成工时：{{ donePercentText }}</span>
      <span class="allocated">/ 已分配工时：{{ allocatedPercentText }}</span>
    </div>

    <!-- 列表整页渲染（隐藏内部工具条，避免重复） -->
    <ScrumSprintBacklogList
      ref="listRef"
      :asDrawer="false"
      :showToolbar="false"
      :sprintIdProp="boundSprintId"
      :sprintNameProp="boundSprintName"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { scrumProductsApi } from '/@/api/business/scrum-products/scrum-products-api';
import { scrumSprintsApi } from '/@/api/business/scrum-sprints/scrum-sprints-api';
import ScrumSprintBacklogList from './scrum-sprint-backlog-list.vue';
import { smartSentry } from '/@/lib/smart-sentry';

const route = useRoute();
const productOptions = ref([]);
const sprintOptions = ref([]);
const currentProductId = ref(undefined);
const currentSprintId = ref(undefined);
const currentSprintName = ref('');

// 供列表组件绑定的响应式值
const boundSprintId = ref(undefined);
const boundSprintName = ref('');

// 顶部展示用统计
const teamName = ref('');
const manDayTotal = ref('0'); // 480（总小时）
const manDayUsed = ref('0');  // 94（已用小时）
const donePercentText = ref('—');
const allocatedPercentText = ref('—');

async function loadProducts(visible){
  if (!visible) return;
  if (productOptions.value.length) return;
  try{
    const res = await scrumProductsApi.queryPage({ pageNum: 1, pageSize: 200 });
    const list = res?.data?.list || [];
    productOptions.value = list.map(p => ({ id: p.id, name: p.productName || `产品${p.id}` }));
  }catch(e){ smartSentry.captureError(e); }
}

async function loadSprints(visible){
  if (!visible) return;
  if (!currentProductId.value) return;
  try{
    const res = await scrumSprintsApi.queryPage({ pageNum: 1, pageSize: 200, productId: currentProductId.value });
    const list = res?.data?.list || [];
    sprintOptions.value = list.map(s => ({ id: s.id, name: s.sprintName || `Sprint${s.id}` }));
  }catch(e){ smartSentry.captureError(e); }
}

function onProductChange(){
  sprintOptions.value = [];
  currentSprintId.value = undefined;
  teamName.value = '';
  manDayTotal.value = '0';
  manDayUsed.value = '0';
  donePercentText.value = '—';
  allocatedPercentText.value = '—';
}

function onSprintChange(){
  // 同步展示名称
  const opt = sprintOptions.value.find(o => o.id === currentSprintId.value);
  currentSprintName.value = opt?.name || '';
  // 拉取该产品下所有迭代，再找对应的迭代统计（后端分页查询不支持sprintId筛选）
  refreshSprintSummary();
}

async function refreshSprintSummary(){
  try{
    const res = await scrumSprintsApi.queryPage({ pageNum: 1, pageSize: 200, productId: currentProductId.value });
    const list = res?.data?.list || [];
    const s = list.find(x => x.id === currentSprintId.value);
    if (!s){ teamName.value=''; manDayTotal.value='0'; manDayUsed.value='0'; donePercentText.value='—'; allocatedPercentText.value='—'; return; }
    teamName.value = s.teamName || '';
    // 右图显示的是小时：已用/总容量（人日*8）
    const totalHours = (s.totalManDay ?? 0) * 8;
    const usedHours = s.consumedHours ?? 0;
    manDayTotal.value = String(totalHours);
    manDayUsed.value = String(usedHours);
    // 完成与分配占比文案（近似计算）：
    const donePercent = s.sprintProgress ?? 0; // 以迭代进度做完成度近似
    const allocatedPercent = totalHours ? ((usedHours / totalHours) * 100).toFixed(1) : 0;
    donePercentText.value = `${donePercent}%`;
    allocatedPercentText.value = `${allocatedPercent}% (${usedHours}/${totalHours})`;
  }catch(e){ smartSentry.captureError(e); }
}

function applySelection(){
  // 绑定到列表组件（页面模式会监听并自动查询）
  const s = sprintOptions.value.find(x => x.id === currentSprintId.value);
  boundSprintId.value = currentSprintId.value;
  boundSprintName.value = s ? s.name : '';
}

function openCreate(){
  // 壳页触发列表内部的新建弹窗
  listRef.value?.openCreate();
}

function openEmpty(){
  // 清空绑定，仅显示空列表或全量查询（由后端决定）
  boundSprintId.value = undefined;
  boundSprintName.value = '';
}

onMounted(async () => {
  // 预加载产品以便首次下拉可选
  await loadProducts(true);
  const sId = route?.query?.sprintId ? Number(route.query.sprintId) : undefined;
  const sName = route?.query?.sprintName || '';
  if (sId) {
    // 无法直接查询某个sprint，先定位产品（通过后续刷新统计）
    boundSprintId.value = sId;
    boundSprintName.value = sName;
    currentSprintId.value = sId;
    currentSprintName.value = sName;
  }
});

// 当产品变化时，清空迭代并刷新迭代下拉
watch(currentProductId, () => {
  sprintOptions.value = [];
  currentSprintId.value = undefined;
});

const listRef = ref();
</script>

<style scoped>
.page-wrap{ padding: 8px 0; }
.top-filter{ margin: 0 0 8px 0; }
.label{ color:#666; margin-right:6px; }
.stat{ color:#666; margin-left:10px; }
.summary-line{ margin: 8px 0 12px; color:#666; }
.summary-line .done{ color:#f5222d; margin-right:8px; }
.summary-line .allocated{ color:#1890ff; }
</style>