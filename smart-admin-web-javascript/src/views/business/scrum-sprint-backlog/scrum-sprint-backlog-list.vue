<!--
  * Sprint待办事项 列表
  * 支持两种模式：
  *  - 抽屉模式（默认）：asDrawer=true，用于在其他页面内弹出
  *  - 页面模式：asDrawer=false，直接作为独立页面渲染
-->
<template>
  <!-- 抽屉模式 -->
  <div v-if="asDrawer">
    <a-drawer
      :title="`Sprint待办事项：` + sprintName"
      :width="1000"
      :open="visibleFlag"
      @close="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
    >
      <a-card size="small" :bordered="false" :hoverable="true">
        <a-row v-if="showToolbar" class="smart-table-btn-block">
          <div class="smart-table-operate-block">
            <a-button type="primary" size="small" @click="showForm()">新建</a-button>
            <a-input v-model:value="queryForm.productName" placeholder="产品名称" style="width:180px;margin-left:8px;" allow-clear />
            <a-input v-model:value="queryForm.sprintName" placeholder="迭代名称" style="width:180px;margin-left:8px;" allow-clear />
            <a-button type="default" size="small" style="margin-left:8px;" @click="onSearch">查询</a-button>
          </div>
        </a-row>

        <a-table
          size="small"
          :scroll="{ x: 1500, y: 600 }"
          :dataSource="tableData"
          :columns="columns"
          rowKey="id"
          bordered
          :loading="tableLoading"
          :pagination="false"
        >
          <template #bodyCell="{ text, record, column }">
            <template v-if="column.dataIndex === 'productBacklogName'">
              <span>{{ record.productBacklogName || record.productName || '-' }}</span>
            </template>
            <template v-else-if="column.dataIndex === 'memberStatus'">
              <div>
                <span :style="{ color: record.memberStatus === '开发中' ? '#52c41a' : (record.memberStatus === '已完成' ? '#fa541c' : '#999') }">{{ record.memberStatus || '-' }}</span>
                <a-tag v-if="record.memberName" style="margin-left:6px;">{{ record.memberName }}</a-tag>
              </div>
            </template>
            <template v-else-if="column.dataIndex === 'surveyStatus'">
              <span>{{ record.surveyStatus || '-' }}</span>
            </template>
            <template v-else-if="column.dataIndex === 'planHours'">
              <span style="color:#52c41a">{{ (record.estimatedHours || record.planHours || '-') }}</span>
            </template>
            <template v-else-if="column.dataIndex === 'consumeHours'">
              <span style="color:#fa541c">{{ record.actualHours || record.consumeHours || '-' }}</span>
            </template>
            <template v-else-if="column.dataIndex === 'planStart'">
              <span style="color:#52c41a">{{ sprintPlanStart || '-' }}</span>
            </template>
            <template v-else-if="column.dataIndex === 'planEnd'">
              <span style="color:#fa541c">{{ sprintPlanEnd || '-' }}</span>
            </template>
            <template v-else-if="column.dataIndex === 'actualStartEnd'">
              <div>
                <div style="color:#52c41a">{{ record.createTime || '-' }}</div>
                <div style="color:#fa541c">{{ record.updateTime || '-' }}</div>
              </div>
            </template>
            <template v-else-if="column.dataIndex === 'logCount'">
              <a-tag color="processing">{{ (record.logCount || 0) + '次' }}</a-tag>
            </template>
            <template v-else-if="column.dataIndex === 'action'">
              <div class="smart-table-operate">
                <a-button @click="showForm(record)" type="link">编辑</a-button>
                <a-button @click="onDelete(record)" danger type="link">删除</a-button>
              </div>
            </template>
          </template>
        </a-table>

        <div class="smart-query-table-page">
          <a-pagination
            showSizeChanger
            showQuickJumper
            show-less-items
            :pageSizeOptions="PAGE_SIZE_OPTIONS"
            :defaultPageSize="queryForm.pageSize"
            v-model:current="queryForm.pageNum"
            v-model:pageSize="queryForm.pageSize"
            :total="total"
            @change="queryData"
            @showSizeChange="queryData"
            :show-total="(total) => `共${total}条`"
          />
        </div>

        <ScrumSprintBacklogForm ref="formRef" @reloadList="queryData" />
      </a-card>
    </a-drawer>
  </div>

  <!-- 页面模式：直接渲染内容，不使用抽屉 -->
  <div v-else>
    <a-card size="small" :bordered="false" :hoverable="true" class="smart-margin-top10">
      <a-row v-if="showToolbar" class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button type="primary" size="small" @click="showForm()">新建</a-button>
          <a-input v-model:value="queryForm.productName" placeholder="产品名称" style="width:180px;margin-left:8px;" allow-clear />
          <a-input v-model:value="queryForm.sprintName" placeholder="迭代名称" style="width:180px;margin-left:8px;" allow-clear />
          <a-button type="default" size="small" style="margin-left:8px;" @click="onSearch">查询</a-button>
        </div>
      </a-row>

      <a-table
        size="small"
        :scroll="{ x: 1500, y: 800 }"
        :dataSource="tableData"
        :columns="columns"
        rowKey="id"
        bordered
        :loading="tableLoading"
        :pagination="false"
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'productBacklogName'">
            <span>{{ record.productBacklogName || record.productName || '-' }}</span>
          </template>
          <template v-else-if="column.dataIndex === 'memberStatus'">
            <div>
              <span :style="{ color: record.memberStatus === '开发中' ? '#52c41a' : (record.memberStatus === '已完成' ? '#fa541c' : '#999') }">{{ record.memberStatus || '-' }}</span>
              <a-tag v-if="record.memberName" style="margin-left:6px;">{{ record.memberName }}</a-tag>
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'surveyStatus'">
            <span>{{ record.surveyStatus || '-' }}</span>
          </template>
          <template v-else-if="column.dataIndex === 'planHours'">
            <span style="color:#52c41a">{{ (record.estimatedHours || record.planHours || '-') }}</span>
          </template>
          <template v-else-if="column.dataIndex === 'consumeHours'">
            <span style="color:#fa541c">{{ record.actualHours || record.consumeHours || '-' }}</span>
          </template>
          <template v-else-if="column.dataIndex === 'planStart'">
            <span style="color:#52c41a">{{ sprintPlanStart || '-' }}</span>
          </template>
          <template v-else-if="column.dataIndex === 'planEnd'">
            <span style="color:#fa541c">{{ sprintPlanEnd || '-' }}</span>
          </template>
          <template v-else-if="column.dataIndex === 'actualStartEnd'">
            <div>
              <div style="color:#52c41a">{{ record.createTime || '-' }}</div>
              <div style="color:#fa541c">{{ record.updateTime || '-' }}</div>
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'logCount'">
            <a-tag color="processing">{{ (record.logCount || 0) + '次' }}</a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <div class="smart-table-operate">
              <a-button @click="showForm(record)" type="link">编辑</a-button>
              <a-button @click="onDelete(record)" danger type="link">删除</a-button>
            </div>
          </template>
        </template>
      </a-table>

      <div class="smart-query-table-page">
        <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="queryData"
          @showSizeChange="queryData"
          :show-total="(total) => `共${total}条`"
        />
      </div>

      <ScrumSprintBacklogForm ref="formRef" @reloadList="queryData" />
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, watch } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { SmartLoading } from '/@/components/framework/smart-loading';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { smartSentry } from '/@/lib/smart-sentry';
import { scrumSprintBacklogApi } from '/@/api/business/scrum-sprint-backlog/scrum-sprint-backlog-api';
import ScrumSprintBacklogForm from './scrum-sprint-backlog-form.vue';
import { useRoute } from 'vue-router';
import { scrumSprintsApi } from '/@/api/business/scrum-sprints/scrum-sprints-api';

const props = defineProps({
  asDrawer: { type: Boolean, default: true },
  sprintIdProp: { type: Number, default: undefined },
  sprintNameProp: { type: String, default: '' },
  showToolbar: { type: Boolean, default: true },
});

const visibleFlag = ref(false);
const sprintId = ref(null);
const sprintName = ref('');

// 路由参数兼容：仅在抽屉模式或未传入prop时读取
const route = useRoute();
onMounted(() => {
  try {
    if (!props.asDrawer) {
      // 页面模式下直接显示内容
      visibleFlag.value = true;
      sprintId.value = props.sprintIdProp ?? null;
      sprintName.value = props.sprintNameProp || '';
      queryForm.pageNum = 1;
      queryData();
    } else {
      const sId = route?.query?.sprintId ? Number(route.query.sprintId) : undefined;
      const sName = route?.query?.sprintName || '';
      if (sId) {
        show(sId, sName);
      }
    }
  } catch (e) {
    // ignore
  }
});

// 监听外部传入的 sprintId（页面模式）
watch(() => props.sprintIdProp, (val) => {
  if (!props.asDrawer) {
    sprintId.value = val ?? null;
    queryForm.pageNum = 1;
    queryData();
  }
});

const columns = ref([
  { title: '代办名称', dataIndex: 'backlogTitle', ellipsis: true, width: 220 },
  { title: '产品代办', dataIndex: 'productBacklogName', ellipsis: true, width: 200 },
  { title: '成员状态', dataIndex: 'memberStatus', width: 140 },
  { title: '审批', dataIndex: 'surveyStatus', width: 90 },
  { title: '计划工时', dataIndex: 'planHours', width: 110 },
  { title: '工时消耗', dataIndex: 'consumeHours', width: 110 },
  { title: '计划开始', dataIndex: 'planStart', width: 120 },
  { title: '计划完成', dataIndex: 'planEnd', width: 120 },
  { title: '实际开始/完成', dataIndex: 'actualStartEnd', width: 140 },
  { title: '日志数', dataIndex: 'logCount', width: 90 },
  { title: '操作', dataIndex: 'action', fixed: 'right', width: 140 },
]);

const queryFormState = {
  pageNum: 1,
  pageSize: 10,
  productName: '',
  sprintName: '',
};
const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

const formRef = ref();

function show(id, name){
  visibleFlag.value = true;
  sprintId.value = id;
  sprintName.value = name || '';
  queryForm.pageNum = 1;
  queryData();
}

function openCreate(){
  showForm();
}

function showForm(record){
  formRef.value.show(record);
}

function onClose(){
  visibleFlag.value = false;
}

function onSearch(){
  queryForm.pageNum = 1;
  queryData();
}

async function queryData(){
  try{
    tableLoading.value = true;
    const params = { ...queryForm, pageNum: queryForm.pageNum, pageSize: queryForm.pageSize, sprintId: sprintId.value };
    const res = await scrumSprintBacklogApi.queryPage(params);
    const data = res?.data || {};
    tableData.value = data.list || [];
    total.value = data.total || 0;
  }catch(e){
    smartSentry.captureError(e);
    message.error('加载列表失败');
  }finally{
    tableLoading.value = false;
  }
}

async function onDelete(record){
  Modal.confirm({
    title: '删除确认',
    content: `确认删除待办：${record.backlogTitle || record.productBacklogName || record.id}?`,
    onOk: async () => {
      try{
        SmartLoading.show();
        await scrumSprintBacklogApi.delete({ id: record.id });
        message.success('删除成功');
        queryData();
      }catch(e){
        smartSentry.captureError(e);
        message.error('删除失败');
      }finally{
        SmartLoading.hide();
      }
    }
  });
}
</script>

<style scoped>
/* 保持操作列固定在右侧（Ant Design Vue通过 fixed: 'right' 实现） */
.smart-table-operate { display: flex; gap: 8px; }
</style>