<!--
<<<<<<<< HEAD:smart-admin-master/smart-admin-web-javascript/src/views/business/scrum/sys-activities/sys-activities-list.vue
  * 系统动态表（全局操作记录）
  *
  * @Author:    a
  * @Date:      2025-09-30 10:32:40
  * @Copyright  a
========
  * 站内信/邮件模板表
  *
  * @Author:    thr
  * @Date:      2025-10-12 14:33:45
  * @Copyright  wu
>>>>>>>> origin/thr_10/15:smart-admin-master/smart-admin-web-javascript/src/views/scrum/template-list.vue
-->
<template>
    <!---------- 查询表单form begin ----------->
    <a-form class="smart-query-form">
        <a-row class="smart-query-form-row">
<<<<<<<< HEAD:smart-admin-master/smart-admin-web-javascript/src/views/business/scrum/sys-activities/sys-activities-list.vue
========
            <a-form-item label="主键" class="smart-query-form-item">
                <a-input style="width: 200px" v-model:value="queryForm.id" placeholder="主键" />
            </a-form-item>
            <a-form-item label="创建人（sysUser.id，数值型匹配）" class="smart-query-form-item">
                <a-input style="width: 200px" v-model:value="queryForm.creator" placeholder="创建人（sysUser.id，数值型匹配）" />
            </a-form-item>
            <a-form-item label="修改人（sysUser.id，数值型匹配）" class="smart-query-form-item">
                <a-input style="width: 200px" v-model:value="queryForm.updater" placeholder="修改人（sysUser.id，数值型匹配）" />
            </a-form-item>
>>>>>>>> origin/thr_10/15:smart-admin-master/smart-admin-web-javascript/src/views/scrum/template-list.vue
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

<<<<<<<< HEAD:smart-admin-master/smart-admin-web-javascript/src/views/business/scrum/sys-activities/sys-activities-list.vue
        <SysActivitiesForm  ref="formRef" @reloadList="queryData"/>
========
        <TemplateForm  ref="formRef" @reloadList="queryData"/>
>>>>>>>> origin/thr_10/15:smart-admin-master/smart-admin-web-javascript/src/views/scrum/template-list.vue

    </a-card>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { message, Modal } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
<<<<<<<< HEAD:smart-admin-master/smart-admin-web-javascript/src/views/business/scrum/sys-activities/sys-activities-list.vue
    import { sysActivitiesApi } from '/@/api/business/scrum/sys-activities/sys-activities-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import SysActivitiesForm from './sys-activities-form.vue';
    //import FilePreview from '/@/components/support/file-preview/index.vue'; // 图片预览组件
========
    import { templateApi } from '/@/api/business/template/template-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import TemplateForm from './template-form.vue';
>>>>>>>> origin/thr_10/15:smart-admin-master/smart-admin-web-javascript/src/views/scrum/template-list.vue

    // ---------------------------- 表格列 ----------------------------

    const columns = ref([
        {
<<<<<<<< HEAD:smart-admin-master/smart-admin-web-javascript/src/views/business/scrum/sys-activities/sys-activities-list.vue
            title: '动态ID（主键）',
========
            title: '主键',
>>>>>>>> origin/thr_10/15:smart-admin-master/smart-admin-web-javascript/src/views/scrum/template-list.vue
            dataIndex: 'id',
            ellipsis: true,
        },
        {
<<<<<<<< HEAD:smart-admin-master/smart-admin-web-javascript/src/views/business/scrum/sys-activities/sys-activities-list.vue
            title: '操作用户ID（关联sys_user.id）',
            dataIndex: 'userId',
            ellipsis: true,
        },
        {
            title: '用户名（冗余，便于前端显示）',
            dataIndex: 'userName',
            ellipsis: true,
        },
        {
            title: '操作类型（add=添加，delete=删除，update=修改，view=查看）',
            dataIndex: 'actionType',
            ellipsis: true,
        },
        {
            title: '操作对象类型（product=产品，sprint=Sprint，sprint_backlog=Sprint待办）',
            dataIndex: 'targetType',
            ellipsis: true,
        },
        {
            title: '操作对象ID',
            dataIndex: 'targetId',
            ellipsis: true,
        },
        {
            title: '动态内容（如"Rick添加Sprint待办：9.23考试""李华删除产品：XX系统"）',
            dataIndex: 'activityContent',
            ellipsis: true,
        },
        {
            title: '操作时间',
            dataIndex: 'createTime',
            ellipsis: true,
========
            title: '模板标识（唯一，如sprint_start_notice）',
            dataIndex: 'templateCode',
            ellipsis: true,
        },
        {
            title: '类型（1=站内信，2=邮件）',
            dataIndex: 'templateType',
            ellipsis: true,
        },
        {
            title: '模板名称（如Sprint启动通知模板）',
            dataIndex: 'templateName',
            ellipsis: true,
        },
        {
            title: '模板内容（支持变量占位符，如${sprintName}）',
            dataIndex: 'templateContent',
            ellipsis: true,
        },
        {
            title: '创建时间',
            dataIndex: 'createTime',
            ellipsis: true,
        },
        {
            title: '更新时间',
            dataIndex: 'updateTime',
            ellipsis: true,
        },
        {
            title: '创建人（sys_user.id，数值型匹配）',
            dataIndex: 'creator',
            ellipsis: true,
        },
        {
            title: '修改人（sys_user.id，数值型匹配）',
            dataIndex: 'updater',
            ellipsis: true,
>>>>>>>> origin/thr_10/15:smart-admin-master/smart-admin-web-javascript/src/views/scrum/template-list.vue
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
<<<<<<<< HEAD:smart-admin-master/smart-admin-web-javascript/src/views/business/scrum/sys-activities/sys-activities-list.vue
========
        id: undefined, //主键
        creator: undefined, //创建人（sysUser.id，数值型匹配）
        updater: undefined, //修改人（sysUser.id，数值型匹配）
>>>>>>>> origin/thr_10/15:smart-admin-master/smart-admin-web-javascript/src/views/scrum/template-list.vue
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
<<<<<<<< HEAD:smart-admin-master/smart-admin-web-javascript/src/views/business/scrum/sys-activities/sys-activities-list.vue
            let queryResult = await sysActivitiesApi.queryPage(queryForm);
========
            let queryResult = await templateApi.queryPage(queryForm);
>>>>>>>> origin/thr_10/15:smart-admin-master/smart-admin-web-javascript/src/views/scrum/template-list.vue
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
<<<<<<<< HEAD:smart-admin-master/smart-admin-web-javascript/src/views/business/scrum/sys-activities/sys-activities-list.vue
            await sysActivitiesApi.delete(data.id);
========
            await templateApi.delete(data.id);
>>>>>>>> origin/thr_10/15:smart-admin-master/smart-admin-web-javascript/src/views/scrum/template-list.vue
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
<<<<<<<< HEAD:smart-admin-master/smart-admin-web-javascript/src/views/business/scrum/sys-activities/sys-activities-list.vue
            await sysActivitiesApi.batchDelete(selectedRowKeyList.value);
========
            await templateApi.batchDelete(selectedRowKeyList.value);
>>>>>>>> origin/thr_10/15:smart-admin-master/smart-admin-web-javascript/src/views/scrum/template-list.vue
            message.success('删除成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>
