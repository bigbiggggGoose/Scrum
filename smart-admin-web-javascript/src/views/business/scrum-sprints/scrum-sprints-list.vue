<!--
  * 项目迭代表
  *
  * @Author:    cmz
  * @Date:      2025-10-11 22:41:18
  * @Copyright  1
-->
<template>
    <!---------- 查询表单form begin ----------->
    <a-form class="smart-query-form">
        <a-row class="smart-query-form-row">
            <!-- 产品筛选 -->
            <a-form-item class="smart-query-form-item">
                <span class="smart-query-form-item-label">产品</span>
                <a-select
                  v-model:value="queryForm.productId"
                  :options="productOptions"
                  allowClear
                  showSearch
                  optionFilterProp="label"
                  placeholder="请选择产品"
                  style="width:220px"
                  @change="onSearch"
                />
            </a-form-item>
            <!-- 未分配团队筛选 -->
            <a-form-item class="smart-query-form-item smart-margin-left10">
              <a-checkbox v-model:checked="onlyUnassignedTeam" @change="onSearch">显示未分配团队</a-checkbox>
            </a-form-item>
            <!-- 操作按钮 -->
            <a-form-item class="smart-query-form-item">
                <a-button type="primary" @click="onSearch">
                    <template #icon>
                        <SearchOutlined />
                    </template>
                    查询
                </a-button>
                <a-button @click="resetQuery" class="smart-margin-left10">
                    <template #icon>
                        <ReloadOutlined />
                    </template>
                    重置
                </a-button>
            </a-form-item>
        </a-row>
    </a-form>
    <!---------- 查询表单form end ----------->

    <a-card size="small" :bordered="false" :hoverable="true">
        <!---------- 表格操作行 begin ----------->
        <a-row class="smart-table-btn-block">
            <div class="smart-table-operate-block">
                <a-button @click="showForm" type="primary" size="small">
                    <template #icon>
                        <PlusOutlined />
                    </template>
                    新建
                </a-button>
                <a-button @click="confirmBatchDelete" type="primary" danger size="small" :disabled="selectedRowKeyList.length == 0">
                    <template #icon>
                        <DeleteOutlined />
                    </template>
                    批量删除
                </a-button>
            </div>
            <div class="smart-table-setting-block">
                <TableOperator v-model="columns" :tableId="null" :refresh="queryData" />
            </div>
        </a-row>
        <!---------- 表格操作行 end ----------->

        <!---------- 表格 begin ----------->
        <a-table
            size="small"
            :scroll="{ x: 'max-content', y: 800 }"
            :dataSource="tableData"
            :columns="columns"
            rowKey="id"
            bordered
            :loading="tableLoading"
            :pagination="false"
            :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
        >
            <template #bodyCell="{ text, record, column }">


                <template v-if="column.dataIndex === 'action'">
                    <div class="smart-table-operate">
                        <a-button @click="showForm(record)" type="link">编辑</a-button>
                        <a-button @click="onDelete(record)" danger type="link">删除</a-button>
                    </div>
                </template>
            </template>
        </a-table>
        <!---------- 表格 end ----------->

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

        <ScrumSprintsForm  ref="formRef" @reloadList="queryData"/>
    </a-card>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { message, Modal } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
    import { scrumSprintsApi } from '/@/api/business/scrum-sprints/scrum-sprints-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import ScrumSprintsForm from './scrum-sprints-form.vue';
    // import ScrumSprintBacklogList from '/@/views/business/scrum-sprint-backlog/scrum-sprint-backlog-list.vue';
    // 新增：产品列表API & 路由
    import { scrumProductsApi } from '/@/api/business/scrum-products/scrum-products-api';
    import { useRoute } from 'vue-router';

    // ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '迭代名称',
    dataIndex: 'sprintName',
    ellipsis: true,
    className: 'nowrap-cell',
  },
  {
    title: '所属产品',
    dataIndex: 'productName',
    ellipsis: true,
    width: 180,
    className: 'nowrap-cell',
  },
  {
    title: '周期',
    dataIndex: 'period',
    ellipsis: true,
    width: 220,
    className: 'nowrap-cell',
  },
  {
    title: '状态',
    dataIndex: 'sprintStatus',
    ellipsis: true,
    width: 100,
    className: 'nowrap-cell',
  },
  {
    title: '进度',
    dataIndex: 'sprintProgress',
    ellipsis: true,
    width: 160,
    className: 'nowrap-cell',
  },
  {
    title: '计划/已用人日',
    dataIndex: 'planConsumed',
    ellipsis: true,
    width: 140,
    className: 'nowrap-cell',
  },
  {
    title: '团队',
    dataIndex: 'teamName',
    ellipsis: true,
    width: 140,
    className: 'nowrap-cell',
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 90,
  },
]);

    // ---------------------------- 查询数据表单和方法 ----------------------------

    const queryFormState = {
        pageNum: 1,
        pageSize: 10,
        productId: undefined,
    };
    // 查询表单form
    const queryForm = reactive({ ...queryFormState });
    // 产品选项
    const productOptions = ref([]);
    // 仅显示未分配团队
    const onlyUnassignedTeam = ref(false);

    // 表格加载loading
    const tableLoading = ref(false);
    // 表格数据
    const tableData = ref([]);
    // 总数
    const total = ref(0);

    // 重置查询条件
    function resetQuery() {
        let pageSize = queryForm.pageSize;
        Object.assign(queryForm, queryFormState);
        queryForm.pageSize = pageSize;
        onlyUnassignedTeam.value = false;
        queryData();
    }

    // 搜索
    function onSearch(){
      queryForm.pageNum = 1;
      queryData();
    }

    // 工具：格式化日期
    function formatDate(val){
      if (!val) return '-';
      try {
        return String(val).substring(0,10);
      } catch(e){ return val; }
    }

    // 工具：根据productId解析产品名称
    function resolveProductName(pid){
      if (!pid) return '—';
      const opt = productOptions.value.find(o => o.value === pid);
      return opt?.label;
    }

    // 加载产品选项
    async function loadProductOptions(){
      try{
        const res = await scrumProductsApi.queryPage({ pageNum: 1, pageSize: 200 });
        const list = res?.data?.list || [];
        productOptions.value = list.map(p => ({ label: p.productName || `产品${p.id}` , value: p.id }));
      }catch(e){
        smartSentry.captureError(e);
      }
    }

    // 从路由应用产品筛选
    function applyRouteProduct(){
      try{
        const route = useRoute();
        const pid = route?.query?.productId;
        queryForm.productId = pid ? Number(pid) : undefined;
      }catch(e){
        // ignore
      }
    }

    // 查询数据
    async function queryData() {
        tableLoading.value = true;
        try {
            let queryResult = await scrumSprintsApi.queryPage(queryForm);
            let list = queryResult.data.list || [];
            // 客户端过滤：未分配团队
            if (onlyUnassignedTeam.value) {
              list = list.filter(r => !r.teamId);
            }
            // 新增：前端映射展示字段
            list = list.map((r) => {
              const team = r.teamName || (r.teamId ? `团队${r.teamId}` : '—');
              return {
                ...r,
                productName: r.productName || resolveProductName(r.productId),
                period: `${formatDate(r.startDate)} ~ ${formatDate(r.endDate)}`,
                planConsumed: `${r.totalManDay ?? 0} / ${(((r.consumedHours ?? 0) / 8)).toFixed(1)}`,
                teamName: team,
              };
            });
            tableData.value = list;
            total.value = queryResult.data.total;
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            tableLoading.value = false;
        }
    }


    onMounted(() => {
      applyRouteProduct();
      loadProductOptions();
      queryData();
    });

    // ---------------------------- 添加/修改 ----------------------------
    const formRef = ref();
    // const backlogListRef = ref();
    
    function showForm(data) {
        formRef.value.show(data);
    }
    // function showSprintBacklog(record){
    //     backlogListRef.value.show(record.id, record.sprintName);
    // }
    
    // ---------------------------- 单个删除 ----------------------------
    //确认删除
    function onDelete(data){
        Modal.confirm({
            title: '提示',
            content: '确定要删除选吗?',
            okText: '删除',
            okType: 'danger',
            onOk() {
                requestDelete(data);
            },
            cancelText: '取消',
            onCancel() {},
        });
    }

    //请求删除
    async function requestDelete(data){
        SmartLoading.show();
        try {
            let deleteForm = {
                goodsIdList: selectedRowKeyList.value,
            };
            await scrumSprintsApi.delete(data.id);
            message.success('删除成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }

    // ---------------------------- 批量删除 ----------------------------

    // 选择表格行
    const selectedRowKeyList = ref([]);

    function onSelectChange(selectedRowKeys) {
        selectedRowKeyList.value = selectedRowKeys;
    }

    // 批量删除
    function confirmBatchDelete() {
        Modal.confirm({
            title: '提示',
            content: '确定要批量删除这些数据吗?',
            okText: '删除',
            okType: 'danger',
            onOk() {
                requestBatchDelete();
            },
            cancelText: '取消',
            onCancel() {},
        });
    }

    //请求批量删除
    async function requestBatchDelete() {
        try {
            SmartLoading.show();
            await scrumSprintsApi.batchDelete(selectedRowKeyList.value);
            message.success('删除成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>

<style scoped>
.nowrap-cell {
  white-space: nowrap;
}
</style>
