<!--
  * 部门管理
  *
  * @Author:    oyt
  * @Date:      2025-10-11 17:57:33
  * @Copyright  oyt
-->
<template>
    <!---------- 查询表单form begin ----------->
    <a-form class="smart-query-form">
        <a-row class="smart-query-form-row">
            <a-form-item label="部门名称" class="smart-query-form-item">
                <a-input style="width: 200px" v-model:value="queryForm.deptName" placeholder="部门名称" />
            </a-form-item>
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
      <a-row :gutter="16">
        <!-- 左侧部门树 -->
        <a-col :span="6">
          <a-card size="small" title="部门列表" :bordered="true">
            <a-tree
              :treeData="deptTreeNodes"
              :selectedKeys="treeSelectedKeys"
              :defaultExpandAll="true"
              @select="onTreeSelect"
            />
          </a-card>
        </a-col>

        <!-- 右侧子部门表格 -->
        <a-col :span="18">
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

          <SysDeptForm ref="formRef" @reloadList="queryData" />
        </a-col>
      </a-row>
    </a-card>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { message, Modal } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
    import { sysDeptApi } from '/@/api/scrum/sys-dept-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import SysDeptForm from './sys-dept-form.vue';

    // ---------------------------- 表格列 ----------------------------

    const columns = ref([
        {
            title: '部门标识',
            dataIndex: 'deptCode',
            ellipsis: true,
        },
        {
            title: '部门名称',
            dataIndex: 'deptName',
            ellipsis: true,
        },
        {
            title: '部门等级',
            dataIndex: 'level',
            ellipsis: true,
        },
        {
            title: '备注',
            dataIndex: 'remark',
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
        deptName: undefined, //部门名称
        pageNum: 1,
        pageSize: 10,
    };
    // 查询表单form
    const queryForm = reactive({ ...queryFormState });
    // 表格加载loading
    const tableLoading = ref(false);
    const deptAllList = ref([]);
    const deptTreeNodes = ref([]);
    const treeSelectedKeys = ref([]);
    const selectedDeptId = ref(0);
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
    async function loadAllAndBuildTree() {
        tableLoading.value = true;
        try {
            const res = await sysDeptApi.listAll();
            const data = res.data || [];
            deptAllList.value = data;
            // 构造树节点
            const buildTree = (list, parentId) => {
                const children = list.filter((e) => e.parentId === parentId);
                if (!children || children.length === 0) return [];
                return children.map((e) => ({
                    title: e.deptName,
                    key: e.id,
                    children: buildTree(list, e.id),
                }));
            };
            const topNodes = buildTree(data, 0);
            deptTreeNodes.value = topNodes;
            // 默认选中第一个顶级部门
            if (topNodes && topNodes.length > 0) {
                selectedDeptId.value = topNodes[0].key;
                treeSelectedKeys.value = [selectedDeptId.value];
            } else {
                selectedDeptId.value = 0;
                treeSelectedKeys.value = [];
            }
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            tableLoading.value = false;
        }
    }

    // 根据选择的部门过滤子部门并分页
    async function queryData() {
        tableLoading.value = true;
        try {
            const filtered = deptAllList.value.filter((e) => {
                const parentMatch = selectedDeptId.value ? e.parentId === selectedDeptId.value : e.parentId === 0;
                const nameMatch = !queryForm.deptName || (e.deptName && e.deptName.indexOf(queryForm.deptName) > -1);
                return parentMatch && nameMatch;
            });
            total.value = filtered.length;
            const start = (queryForm.pageNum - 1) * queryForm.pageSize;
            tableData.value = filtered.slice(start, start + queryForm.pageSize);
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            tableLoading.value = false;
        }
    }


    onMounted(loadAllAndBuildTree);

    function onTreeSelect(keys) {
        treeSelectedKeys.value = keys;
        selectedDeptId.value = keys && keys.length > 0 ? keys[0] : 0;
        queryForm.pageNum = 1;
        queryData();
    }

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
            await sysDeptApi.delete(data.id);
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
            await sysDeptApi.batchDelete(selectedRowKeyList.value);
            message.success('删除成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>
