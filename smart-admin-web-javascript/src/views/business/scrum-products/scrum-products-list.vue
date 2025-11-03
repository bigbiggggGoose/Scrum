<!--
  * 产品表
  *
  * @Author:    cmz
  * @Date:      2025-10-11 22:39:41
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

        <ScrumProductsForm  ref="formRef" @reloadList="queryData"/>

    </a-card>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { message, Modal } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
    import { scrumProductsApi } from '/@/api/business/scrum-products/scrum-products-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import ScrumProductsForm from './scrum-products-form.vue';

    // ---------------------------- 表格列 ----------------------------

    // const columns = ref([
    //     {
    //         title: '产品ID（主键）',
    //         dataIndex: 'id',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '产品名称',
    //         dataIndex: 'productName',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '产品编号（唯一）',
    //         dataIndex: 'productCode',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '产品负责人（PO，关联sys_user.id）',
    //         dataIndex: 'productOwnerId',
    //         ellipsis: true,
    //     },
    //     {
    //         title: 'Scrum Master（关联sys_user.id）',
    //         dataIndex: 'scrumMasterId',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '负责团队ID（关联scrum_teams.id）',
    //         dataIndex: 'teamId',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '产品来源（如客户需求、内部立项）',
    //         dataIndex: 'productSource',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '状态（1=未开始，2=进行中，3=未完成，4=已完成，5=已关闭，6=已挂起，7=已延期）',
    //         dataIndex: 'productStatus',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '产品进度（0-100）',
    //         dataIndex: 'productProgress',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '总人日',
    //         dataIndex: 'totalManDay',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '产品价值',
    //         dataIndex: 'productValue',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '产品目标',
    //         dataIndex: 'productGoal',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '用户故事',
    //         dataIndex: 'userStory',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '验收标准',
    //         dataIndex: 'acceptanceStandard',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '边界设定（DOD）',
    //         dataIndex: 'boundary',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '干系人',
    //         dataIndex: 'stakeholders',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '用户和客户',
    //         dataIndex: 'customers',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '计划开始日期',
    //         dataIndex: 'planStartDate',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '计划结束日期',
    //         dataIndex: 'planEndDate',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '项目所需总天数',
    //         dataIndex: 'totalWorkDays',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '已工作天数',
    //         dataIndex: 'workedDays',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '产品备注',
    //         dataIndex: 'productRemarks',
    //         ellipsis: true,
    //     },
    //     {
    //         title: '逻辑删除（0=未删，1=已删）',
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
    {
        title: '产品名称',
        dataIndex: 'productName',
        ellipsis: true,
    },
    {
        title: '产品编号',
        dataIndex: 'productCode',
        ellipsis: true,
    },
    {
        title: '产品负责人',
        dataIndex: 'productOwnerId',
        ellipsis: true,
    },
    {
        title: 'Scrum Master',
        dataIndex: 'scrumMasterId',
        ellipsis: true,
    },
    {
        title: '状态',
        dataIndex: 'productStatus',
        ellipsis: true,
    },
    {
        title: '进度(%)',
        dataIndex: 'productProgress',
        ellipsis: true,
    },
    {
        title: '操作',
        dataIndex: 'action',
        fixed: 'right',
        width: 100,
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
            let queryResult = await scrumProductsApi.queryPage(queryForm);
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
            await scrumProductsApi.delete(data.id);
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
            await scrumProductsApi.batchDelete(selectedRowKeyList.value);
            message.success('删除成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>
