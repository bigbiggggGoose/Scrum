<!--
  * 产品待办事项表
  *
  * @Author:    cmz
  * @Date:      2025-10-11 22:40:46
  * @Copyright  1
-->
<template>
    <!---------- 查询表单form begin ----------->
    <a-form class="smart-query-form">
        <a-row class="smart-query-form-row">
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
            :scroll="{ y: 800 }"
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

        <ScrumProductBacklogsForm  ref="formRef" @reloadList="queryData"/>

    </a-card>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { message, Modal } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
    import { scrumProductBacklogsApi } from '/@/api/business/scrum-product-backlogs/scrum-product-backlogs-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import ScrumProductBacklogsForm from './scrum-product-backlogs-form.vue';

    // ---------------------------- 表格列 ----------------------------

    // const columns = ref([
    //     {
    //         title: '待办ID（主键）',
    //         dataIndex: 'id',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '所属产品ID（关联scrum_products.id）',
    //         dataIndex: 'productId',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '父待办ID（0=顶层，支撑树形结构）',
    //         dataIndex: 'parentId',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '待办名称（用户故事标题）',
    //         dataIndex: 'backlogName',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '优先级（数字越大优先级越低）',
    //         dataIndex: 'priority',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '人日',
    //         dataIndex: 'manDay',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '待办内容',
    //         dataIndex: 'content',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '分级（最大4级）',
    //         dataIndex: 'level',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '单元格样式：1=靠左白背景，2=居中白背景，3=居中加粗白背景，4=居中加粗蓝背景（#b7dee8）等',
    //         dataIndex: 'cellStyle',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '同级排序权重（数值越小越靠前）',
    //         dataIndex: 'backlogWeight',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '预估工时（小时）',
    //         dataIndex: 'estimatedHours',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '状态（关联sys_dict：todo=待办，in_progress=进行中，completed=已完成）',
    //         dataIndex: 'backlogStatus',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '备注',
    //         dataIndex: 'backlogRemarks',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '逻辑删除',
    //         dataIndex: 'isDeleted',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '创建时间',
    //         dataIndex: 'createTime',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '更新时间',
    //         dataIndex: 'updateTime',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '创建人（关联sys_user.id）',
    //         dataIndex: 'creator',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '修改人（关联sys_user.id）',
    //         dataIndex: 'updater',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '操作',
    //         dataIndex: 'action',
    //         fixed: 'right',
    //         width: 90,
    //     },
    // ]);
const columns = ref([
//   {
//     title: '待办ID',
//     dataIndex: 'id',
//     ellipsis: true,
//   },
  {
    title: '待办名称',
    dataIndex: 'backlogName',
    ellipsis: true,
  },
  {
    title: '优先级',
    dataIndex: 'priority',
    ellipsis: true,
  },
  {
    title: '人日',
    dataIndex: 'manDay',
    ellipsis: true,
  },
  {
    title: '预估工时（小时）',
    dataIndex: 'estimatedHours',
    ellipsis: true,
  },
  {
    title: '状态',
    dataIndex: 'backlogStatus',
    ellipsis: true,
  },
  {
    title: '备注',
    dataIndex: 'backlogRemarks',
    ellipsis: true,
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
    };
    // 查询表单form
    const queryForm = reactive({ ...queryFormState });
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
        queryData();
    }

    // 搜索
    function onSearch(){
      queryForm.pageNum = 1;
      queryData();
    }

    // 查询数据
    async function queryData() {
        tableLoading.value = true;
        try {
            let queryResult = await scrumProductBacklogsApi.queryPage(queryForm);
            tableData.value = queryResult.data.list;
            total.value = queryResult.data.total;
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            tableLoading.value = false;
        }
    }


    onMounted(queryData);

    // ---------------------------- 添加/修改 ----------------------------
    const formRef = ref();

    function showForm(data) {
        formRef.value.show(data);
    }

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
            await scrumProductBacklogsApi.delete(data.id);
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
            await scrumProductBacklogsApi.batchDelete(selectedRowKeyList.value);
            message.success('删除成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>
