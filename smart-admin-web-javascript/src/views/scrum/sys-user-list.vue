<!--
  * 用户信息表
  *
  * @Author:    oyt
  * @Date:      2025-10-11 17:34:30
  * @Copyright  oyt
-->
<template>
    <!---------- 查询表单form begin ----------->
    <a-form class="smart-query-form">
        <a-row class="smart-query-form-row" :gutter="[16, 8]">
            <a-col :xs="24" :sm="12" :md="8" :lg="6">
                <a-form-item label="姓名" class="smart-query-form-item">
                    <a-input v-model:value="queryForm.realName" placeholder="姓名" allowClear style="width: 100%" />
                </a-form-item>
            </a-col>
            <a-col :xs="24" :sm="12" :md="8" :lg="6">
                <a-form-item label="登录名" class="smart-query-form-item">
                    <a-input v-model:value="queryForm.username" placeholder="登录名" allowClear style="width: 100%" />
                </a-form-item>
            </a-col>
            <a-col :xs="24" :sm="12" :md="8" :lg="6">
                <a-form-item label="手机号" class="smart-query-form-item">
                    <a-input v-model:value="queryForm.phone" placeholder="手机号" allowClear style="width: 100%" />
                </a-form-item>
            </a-col>
            <a-col :xs="24" :sm="12" :md="8" :lg="6">
                <a-form-item label="邮件" class="smart-query-form-item">
                    <a-input v-model:value="queryForm.email" placeholder="邮箱" allowClear style="width: 100%" />
                </a-form-item>
            </a-col>
            <a-col :xs="24" :sm="12" :md="8" :lg="6">
                <a-form-item label="部门" class="smart-query-form-item">
                    <a-select v-model:value="queryForm.deptId" :options="deptOptions" allowClear placeholder="请选择" style="width: 100%" />
                </a-form-item>
            </a-col>
            <a-col :xs="24" :sm="12" :md="8" :lg="6">
                <a-form-item label="角色" class="smart-query-form-item">
                    <a-select v-model:value="queryForm.roleId" :options="roleOptions" allowClear placeholder="请选择" style="width: 100%" />
                </a-form-item>
            </a-col>
            <a-col :xs="24" :sm="24" :md="8" :lg="6">
                <a-form-item class="smart-query-form-item">
                    <a-button type="primary" @click="onSearch">
                        <template #icon>
                            <SearchOutlined />
                        </template>
                        搜索
                    </a-button>
                    <a-button @click="resetQuery" class="smart-margin-left10">
                        <template #icon>
                            <ReloadOutlined />
                        </template>
                        清空
                    </a-button>
                </a-form-item>
            </a-col>
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
                <template v-if="column.dataIndex === 'avatarUrl'">
                    <a-avatar :src="record.avatarUrl" :size="32">{{ record.realName?.charAt(0) || 'U' }}</a-avatar>
                </template>
                <template v-else-if="column.dataIndex === 'sex'">
                    <span>{{ record.sex === 1 ? '男' : record.sex === 2 ? '女' : '-' }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'action'">
                    <div class="smart-table-operate">
                        <a-button @click="showDetail(record)" type="link">详情</a-button>
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

        <SysUserForm  ref="formRef" @reloadList="queryData"/>
        <SysUserDetailModal ref="detailRef" />

    </a-card>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { message, Modal } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
    import { sysUserApi } from '/@/api/scrum/sys-user-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import SysUserForm from './sys-user-form.vue';
    import SysUserDetailModal from './sys-user-detail-modal.vue';
    import { sysDeptApi } from '/@/api/scrum/sys-dept-api';
    import { sysRoleApi } from '/@/api/scrum/sys-role-api';

    // ---------------------------- 表格列 ----------------------------

    const columns = ref([
        { title: '角色', dataIndex: 'roleName', ellipsis: true, width: 120 },
        { title: '姓名', dataIndex: 'realName', ellipsis: true, width: 120 },
        { title: '头像', dataIndex: 'avatarUrl', width: 80 },
        { title: 'email', dataIndex: 'email', ellipsis: true },
        { title: '手机', dataIndex: 'phone', ellipsis: true, width: 140 },
        { title: '性别', dataIndex: 'sex', width: 80 },
        { title: '操作', dataIndex: 'action', fixed: 'right', width: 160 },
    ]);

    // ---------------------------- 查询数据表单和方法 ----------------------------

    const queryFormState = {
        realName: undefined, // 姓名
        username: undefined, // 登录名
        phone: undefined, // 手机号
        email: undefined, // 邮箱
        deptId: undefined, // 部门
        roleId: undefined, // 角色
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
            let queryResult = await sysUserApi.queryPage(queryForm);
            tableData.value = queryResult.data.list;
            total.value = queryResult.data.total;
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            tableLoading.value = false;
        }
    }


    // 下拉选项
    const deptOptions = ref([]);
    const roleOptions = ref([]);

    async function loadDeptOptions() {
        try {
            const res = await sysDeptApi.queryPage({ pageNum: 1, pageSize: 200 });
            deptOptions.value = (res.data.list || []).map((d) => ({ label: d.deptName, value: d.id }));
        } catch (e) {
            smartSentry.captureError(e);
        }
    }

    async function loadRoleOptions() {
        try {
            const res = await sysRoleApi.queryPage({ pageNum: 1, pageSize: 200 });
            roleOptions.value = (res.data.list || []).map((r) => ({ label: r.roleName, value: r.id }));
        } catch (e) {
            smartSentry.captureError(e);
        }
    }

    onMounted(async () => {
        await Promise.all([loadDeptOptions(), loadRoleOptions()]);
        await queryData();
    });

    // ---------------------------- 添加/修改 ----------------------------
    const formRef = ref();
    const detailRef = ref();

    function showForm(data) {
        formRef.value.show(data);
    }

    function showDetail(data) {
        detailRef.value.show(data);
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
            await sysUserApi.delete(data.id);
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
            await sysUserApi.batchDelete(selectedRowKeyList.value);
            message.success('删除成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>
