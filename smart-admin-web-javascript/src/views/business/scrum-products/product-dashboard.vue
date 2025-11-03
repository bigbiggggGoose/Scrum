<!--
  * 产品看板
  * 
  * 说明：原型版产品看板，聚合产品、迭代、待办与燃尽图显示。
  * 数据来源：scrumProductsApi / scrumSprintsApi / scrumProductBacklogsApi / sysActivitiesApi
-->
<template>
  <div class="product-dashboard">
    <a-page-header title="产品看板" />

    <a-card class="smart-margin-top10" size="small">
      <div class="top-bar">
        <div class="selector">
          <span class="label">产品选择</span>
          <a-select v-model:value="currentProductId" style="width:220px" :options="productOptions" @change="onProductChange" placeholder="请选择产品" />
        </div>
      </div>

      <div class="summary-grid">
        <div class="summary-left">
          <a-card class="summary-card" size="small">
            <div class="status-banner">进行中</div>
            <div class="summary-items">
              <div class="item"><span class="tag">SPRINT进行中</span><span class="value">x{{ sprintCounting.inProgress }}</span></div>
              <div class="item"><span class="tag">已完成</span><span class="value">x{{ sprintCounting.completed }}</span></div>
              <div class="item"><span class="tag">未开始</span><span class="value">x{{ sprintCounting.notStarted }}</span></div>
              <div class="item cols">
                <a-space>
                  <a-tag>PO: {{ poName || '-' }}</a-tag>
                  <a-tag>Team: {{ teamName || '-' }}</a-tag>
                  <a-tag>SM: {{ smName || '-' }}</a-tag>
                  <a-tag>人数: {{ teamCount || '—' }}</a-tag>
                </a-space>
              </div>
              <div class="item cols">
                <a-space>
                  <a-button size="small" @click="goUserStories">用户故事</a-button>
                  <a-button size="small" @click="goProductGoal">ProductGoal</a-button>
                  <a-button size="small" @click="goPBL">PBL</a-button>
                  <a-button size="small" @click="goDOD">DOD</a-button>
                </a-space>
              </div>
              <div class="item cols">
                <a-space direction="vertical">
                  <div>产品周期：{{ productInfo?.totalManDay ?? 0 }} 人日，{{ productInfo?.planStartDate ?? '-' }} 至 {{ productInfo?.planEndDate ?? '-' }}</div>
                  <div>待办完成：{{ backlogDoneHours }} / {{ backlogTotalHours }}（{{ backlogPercent }}%）</div>
                </a-space>
              </div>
            </div>
          </a-card>
        </div>
        <div class="summary-right">
          <a-card class="burn-card" size="small" title="工时燃烧图">
            <template v-if="hasBacklogs">
              <div id="burndown-chart" class="chart"></div>
            </template>
            <template v-else>
              <div class="empty-chart">暂无燃尽数据</div>
            </template>
          </a-card>
        </div>
      </div>
      
    </a-card>

    <!-- 有待办时展示阶段卡片与动态 -->
    <div class="bottom-grid smart-margin-top10">
      <div class="phase-cards">
        <a-card size="small" title="产品待办" class="phase-wrapper">
          <div class="phase-grid">
            <template v-if="hasBacklogs">
              <a-card size="small" class="phase-card" title="系统设计">
                <p>项目总体进度：{{ productInfo?.productProgress ?? 0 }}%</p>
                <p>负责团队：{{ productInfo?.teamId ?? '-' }}</p>
                <p>执行状态：进行中</p>
              </a-card>
              <a-card size="small" class="phase-card" title="开发阶段">
                <p>项目总体进度：{{ productInfo?.productProgress ?? 0 }}%</p>
                <p>负责团队：{{ productInfo?.teamId ?? '-' }}</p>
                <p>执行状态：进行中</p>
              </a-card>
            </template>
            <template v-else>
              <a-empty description="暂无待办数据" />
            </template>
          </div>
        </a-card>
      </div>
      <div class="activity-card">
        <a-card size="small" title="产品动态">
          <template v-if="activities && activities.length">
            <a-list :data-source="activities">
              <template #renderItem="{ item }">
                <a-list-item>
                  <a-list-item-meta :title="(item.createTime || '') + ' ' + (item.userName || '') + ' ' + (item.actionType || item.action || '')" :description="item.activityContent || item.remark || ''" />
                </a-list-item>
              </template>
            </a-list>
          </template>
          <template v-else>
            <a-empty description="暂无产品动态" />
          </template>
        </a-card>
      </div>
    </div>
    
  </div>
</template>

<script setup>
  import { onMounted, ref, reactive, watch, computed } from 'vue';
  import * as echarts from 'echarts';
  import { useRouter } from 'vue-router';
  import { scrumProductsApi } from '/@/api/business/scrum-products/scrum-products-api';
  import { scrumSprintsApi } from '/@/api/business/scrum-sprints/scrum-sprints-api';
  import { scrumProductBacklogsApi } from '/@/api/business/scrum-product-backlogs/scrum-product-backlogs-api';
  import { sysActivitiesApi } from '/@/api/business/scrum/sys-activities/sys-activities-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const router = useRouter();

  const currentProductId = ref();
  const productOptions = ref([]);
  const productInfo = ref();

  const sprintCounting = reactive({ inProgress: 0, completed: 0, notStarted: 0 });
  const teamCount = ref('—');
  const teamName = ref('');
  const poName = ref('');
  const smName = ref('');

  const backlogTotalHours = ref(0);
  const backlogDoneHours = ref(0);
  const backlogPercent = ref(0);
  const backlogCount = ref(0);

  const hasProduct = computed(() => !!currentProductId.value && !!productInfo.value);
  const hasBacklogs = computed(() => hasProduct.value && backlogCount.value > 0);

  const activities = ref([]);

  // 跳转
  function goUserStories() {
    router.push({ path: '/scrum/product-backlogs/list', query: { productId: currentProductId.value } });
  }
  function goPBL() {
    router.push({ path: '/scrum/product-backlogs/list', query: { productId: currentProductId.value } });
  }
  function goProductGoal() {
    router.push({ path: '/scrum/product-backlogs/list', query: { productId: currentProductId.value } });
  }
  function goDOD() {
    router.push({ path: '/scrum/sprints/list', query: { productId: currentProductId.value } });
  }

  async function loadProductInfo() {
    try {
      if (!currentProductId.value) { productInfo.value = undefined; return; }
      const res = await scrumProductsApi.queryPage({ pageNum: 1, pageSize: 1, productId: currentProductId.value });
      productInfo.value = res?.data?.list?.[0];
      await loadMemberAndTeam();
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function loadMemberAndTeam() {
    try {
      if (!hasProduct.value) { poName.value = smName.value = teamName.value = ''; teamCount.value = '—'; return; }
      poName.value = productInfo.value?.poName || '';
      smName.value = productInfo.value?.smName || '';
      teamName.value = productInfo.value?.teamName || '';
      const count = productInfo.value?.teamMemberCount;
      teamCount.value = typeof count === 'number' ? `${count}人` : '—';
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function loadSprints() {
    try {
      if (!hasProduct.value) { sprintCounting.inProgress = sprintCounting.completed = sprintCounting.notStarted = 0; return; }
      const res = await scrumSprintsApi.queryPage({ pageNum: 1, pageSize: 200 });
      const list = (res?.data?.list || []).filter((s) => s.productId === currentProductId.value);
      sprintCounting.inProgress = list.filter((s) => s.sprintStatus === 'in_progress').length;
      sprintCounting.completed = list.filter((s) => s.sprintStatus === 'completed').length;
      sprintCounting.notStarted = list.filter((s) => s.sprintStatus === 'not_started').length;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function loadActivities() {
    try {
      if (!hasProduct.value) { activities.value = []; return; }
      const res = await sysActivitiesApi.queryPage({ pageNum: 1, pageSize: 10, targetType: 'product', targetId: currentProductId.value });
      activities.value = res?.data?.list || [];
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function loadProducts() {
    try {
      const res = await scrumProductsApi.queryPage({ pageNum: 1, pageSize: 200 });
      const list = res?.data?.list || [];
      productOptions.value = list.map((p) => ({ label: p.productName || `产品${p.id}` , value: p.id }));
      if (!currentProductId.value && productOptions.value.length) {
        currentProductId.value = productOptions.value[0].value;
      }
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function loadBacklogs() {
    try {
      if (!hasProduct.value) { backlogTotalHours.value = backlogDoneHours.value = backlogPercent.value = 0; backlogCount.value = 0; return; }
      const res = await scrumProductBacklogsApi.queryPage({ pageNum: 1, pageSize: 1000 });
      const list = (res?.data?.list || []).filter((b) => b.productId === currentProductId.value);
      backlogCount.value = list.length;
      backlogTotalHours.value = list.reduce((sum, b) => sum + (b.estimatedHours || 0), 0);
      backlogDoneHours.value = list.filter((b) => b.backlogStatus === 'completed').reduce((sum, b) => sum + (b.estimatedHours || 0), 0);
      backlogPercent.value = backlogTotalHours.value ? Math.round((backlogDoneHours.value / backlogTotalHours.value) * 100) : 0;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function buildBurndownData() {
    const start = productInfo.value?.planStartDate ? new Date(productInfo.value.planStartDate) : new Date();
    const end = productInfo.value?.planEndDate ? new Date(productInfo.value.planEndDate) : new Date(Date.now() + 7*24*60*60*1000);
    const days = Math.max(1, Math.ceil((end - start) / (24 * 3600 * 1000)) + 1);

    const x = [];
    for (let i = 0; i < days; i++) {
      const d = new Date(start.getTime() + i * 24 * 3600 * 1000);
      x.push(`${d.getMonth() + 1}-${('0' + d.getDate()).slice(-2)}`);
    }

    const totalHours = backlogTotalHours.value || (productInfo.value?.totalManDay || 0) * 8;
    const ideal = [];
    for (let i = 0; i < days; i++) {
      ideal.push(Math.max(0, Math.round(totalHours - (totalHours / (days - 1)) * i)));
    }

    const todayIndex = Math.min(days - 1, Math.max(0, Math.ceil((Date.now() - start.getTime()) / (24 * 3600 * 1000))));
    const burnedPerDay = todayIndex > 0 ? backlogDoneHours.value / todayIndex : 0;
    const actual = [];
    for (let i = 0; i < days; i++) {
      const burned = Math.min(totalHours, Math.round(burnedPerDay * i));
      actual.push(Math.max(0, totalHours - burned));
    }

    return { x, ideal, actual };
  }

  function renderBurndown() {
    if (!hasBacklogs.value) return;
    const { x, ideal, actual } = buildBurndownData();
    const dom = document.getElementById('burndown-chart');
    if (!dom) return;
    const chart = echarts.init(dom);
    const opt = {
      tooltip: { trigger: 'axis' },
      legend: { data: ['理想燃尽', '实际燃尽'] },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: x },
      yAxis: { type: 'value' },
      series: [
        { name: '理想燃尽', type: 'line', data: ideal, smooth: true, color: '#2f7ed8' },
        { name: '实际燃尽', type: 'line', data: actual, smooth: true, color: '#d62728' },
      ],
    };
    chart.setOption(opt);
  }

  async function reloadAll() {
    if (!currentProductId.value) {
      productInfo.value = undefined;
      sprintCounting.inProgress = sprintCounting.completed = sprintCounting.notStarted = 0;
      backlogTotalHours.value = backlogDoneHours.value = backlogPercent.value = 0;
      backlogCount.value = 0;
      activities.value = [];
      return;
    }
    await loadProductInfo();
    await Promise.all([loadSprints(), loadBacklogs(), loadActivities()]);
    renderBurndown();
  }

  function onProductChange() {
    reloadAll();
  }

  onMounted(async () => {
    await loadProducts();
    if (!currentProductId.value) return;
    await reloadAll();
  });

  watch([backlogTotalHours, backlogDoneHours, () => productInfo.value?.planStartDate, () => productInfo.value?.planEndDate], () => {
    renderBurndown();
  });
</script>

<style scoped>
  .product-dashboard { padding: 10px; }
  .top-bar { display: flex; justify-content: space-between; align-items: center; }
  .selector { display: flex; align-items: center; gap: 10px; }
  .selector .label { color: #666; }
  .empty-hint { padding: 10px; color: #666; }

  .summary-grid { display: grid; grid-template-columns: 1.5fr 1fr; gap: 10px; }
  .summary-left .summary-card { min-height: 220px; }
  .status-banner { writing-mode: vertical-rl; text-orientation: mixed; background:#2b6cb0; color:#fff; padding:6px; border-radius: 4px; width: 30px; text-align:center; float:left; margin-right:10px; }
  .summary-items { margin-left: 40px; display: grid; grid-template-columns: 1fr; gap: 8px; }
  .summary-items .item { display:flex; align-items:center; justify-content: space-between; }
  .summary-items .item.cols { display:block; }
  .summary-items .tag { background:#2b6cb0; color:#fff; padding:4px 8px; border-radius:4px; }
  .summary-items .value { background:#356; color:#fff; padding:4px 8px; border-radius:4px; }

  .burn-card .chart { width: 100%; height: 220px; }
  .burn-card .empty-chart { width: 100%; height: 220px; display:flex; align-items:center; justify-content:center; color:#999; }

  .bottom-grid { display: grid; grid-template-columns: 1.2fr 1fr; gap: 10px; }
  .phase-cards { }
  .phase-wrapper { min-height: 280px; }
  .phase-grid { display:grid; grid-template-columns: 1fr 1fr; gap: 10px; }
  .phase-card { min-height: 120px; }
  .section-title { grid-column: 1 / span 2; font-weight: 600; color:#333; margin-bottom: 4px; }
</style>