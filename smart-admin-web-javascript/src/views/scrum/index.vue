<!--
  * Scrum 角色管理（左列表 + 右表格 CRUD）
  *
  * @Author:    oyt
  * @Date:      2025-10-15
  -->
<template>
  <a-layout class="smart-width-100">
    <a-layout-sider width="240" theme="light" style="background: #fff; padding: 12px;">
      <div style="display:flex; align-items:center; justify-content:space-between; margin-bottom:8px;">
        <span>角色列表</span>
        <a-button type="link" size="small" @click="refreshLeft">刷新</a-button>
      </div>
      <a-input v-model:value="leftFilter" placeholder="筛选角色名称" allow-clear class="smart-margin-bottom10" />
      <a-list :data-source="filteredLeftRoles" :bordered="true" size="small">
        <template #renderItem="{ item }">
          <a-list-item :class="{ 'ant-list-item-selected': item.id === selectedParentId }" @click="selectLeft(item)">
            <div style="display:flex; justify-content:space-between; width:100%">
              <span>{{ item.roleName }}</span>
              <span style="color:#999">{{ item.orderNum }}</span>
            </div>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-sider>

    <a-layout-content style="background:#fff; padding:12px;">
      <div class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button type="primary" @click="onAdd">+ 添加</a-button>
          <a-button danger :disabled="selectedRowKeys.length===0" class="smart-margin-left10" @click="onBatchDelete">批量删除</a-button>
        </div>
        <!-- 右上角查询与重置控件已移除 -->
      </div>

      <a-table
        row-key="id"
        :data-source="tableData"
        :loading="loading"
        :pagination="pagination"
        :row-selection="rowSelection"
      >
        <a-table-column title="角色标识" dataIndex="roleCode" key="roleCode" />
        <a-table-column title="角色名" dataIndex="roleName" key="roleName" />
        <a-table-column title="排序" dataIndex="orderNum" key="orderNum" />
        <a-table-column title="备注" dataIndex="remark" key="remark" />
        <a-table-column title="操作" key="operate" :width="200">
          <template #default="{ record }">
            <a-space>
              <a-button size="small" @click="onDetail(record)">详情</a-button>
              <a-button size="small" type="primary" @click="onEdit(record)">编辑</a-button>
              <a-popconfirm title="确认删除此角色？" @confirm="() => onDelete(record)">
                <a-button size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table-column>
      </a-table>
    </a-layout-content>
  </a-layout>

  <!-- 新增/编辑 -->
  <a-modal :title="modalTitle" v-model:open="formVisible" :confirmLoading="formSubmitting" @ok="onSubmit" @cancel="onCancel">
    <a-form :model="formModel" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }" :rules="formRules" ref="formRef">
      <a-form-item label="角色标识" name="roleCode">
        <a-input v-model:value="formModel.roleCode" />
      </a-form-item>
      <a-form-item label="角色名称" name="roleName">
        <a-input v-model:value="formModel.roleName" />
      </a-form-item>
      <a-form-item label="父角色">
        <a-select v-model:value="formModel.parentId" allow-clear :options="leftRoles.map(r=>({label:r.roleName,value:r.id}))" />
      </a-form-item>
      <a-form-item label="排序">
        <a-input-number v-model:value="formModel.orderNum" :min="0" style="width:100%" />
      </a-form-item>
      <a-form-item label="等级" name="level">
        <a-input-number v-model:value="formModel.level" :min="0" style="width:100%" />
      </a-form-item>
      <a-form-item label="绩效公式">
        <a-input v-model:value="formModel.jxfm" />
      </a-form-item>
      <a-form-item label="待遇描述">
        <a-input v-model:value="formModel.dy" />
      </a-form-item>
      <a-form-item label="状态" name="status">
        <a-select v-model:value="formModel.status">
          <a-select-option :value="1">启用</a-select-option>
          <a-select-option :value="0">禁用</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="备注">
        <a-textarea v-model:value="formModel.remark" :rows="2" />
      </a-form-item>
    </a-form>
  </a-modal>

  <!-- 详情 -->
  <a-modal title="角色详情" v-model:open="detailVisible" :footer="null" @cancel="() => (detailVisible=false)">
    <a-descriptions bordered :column="1" size="small">
      <a-descriptions-item label="ID">{{ detailData?.id }}</a-descriptions-item>
      <a-descriptions-item label="角色标识">{{ detailData?.roleCode }}</a-descriptions-item>
      <a-descriptions-item label="角色名称">{{ detailData?.roleName }}</a-descriptions-item>
      <a-descriptions-item label="父角色ID">{{ detailData?.parentId }}</a-descriptions-item>
      <a-descriptions-item label="排序">{{ detailData?.orderNum }}</a-descriptions-item>
      <a-descriptions-item label="等级">{{ detailData?.level }}</a-descriptions-item>
      <a-descriptions-item label="绩效公式">{{ detailData?.jxfm }}</a-descriptions-item>
      <a-descriptions-item label="待遇描述">{{ detailData?.dy }}</a-descriptions-item>
      <a-descriptions-item label="状态">{{ detailData?.status }}</a-descriptions-item>
      <a-descriptions-item label="备注">{{ detailData?.remark }}</a-descriptions-item>
      <a-descriptions-item label="创建时间">{{ detailData?.createTime }}</a-descriptions-item>
      <a-descriptions-item label="更新时间">{{ detailData?.updateTime }}</a-descriptions-item>
    </a-descriptions>
  </a-modal>
</template>

<script setup>
  import { onMounted, ref, computed } from 'vue';
  import { message } from 'ant-design-vue';
  import _ from 'lodash';
  import { sysRoleApi } from '/@/api/scrum/sys-role-api.js';

  // 左侧角色列表
  const leftRoles = ref([]);
  const leftFilter = ref('');
  const selectedParentId = ref(null);
  // 右侧展示模式：true 表示展示全部；false 表示仅展示当前选中角色
  const showAll = ref(true);
  const filteredLeftRoles = computed(() => {
    if (!leftFilter.value) return leftRoles.value;
    return leftRoles.value.filter((e) => _.includes(e.roleName?.toLowerCase(), leftFilter.value.toLowerCase()));
  });

  function selectLeft(item) {
    // 选中左侧角色后：切换为“仅展示当前角色”模式
    selectedParentId.value = item?.id ?? null;
    showAll.value = false;
    // 重置分页到第一页
    queryForm.pageNum = 1;
    pagination.value.current = 1;
    fetchTable();
  }

  async function refreshLeft() {
    try {
      const res = await sysRoleApi.listAll();
      leftRoles.value = res.data || [];
      // 刷新同时让右侧恢复到“展示全部数据”的默认状态
      showAll.value = true;
      selectedParentId.value = null;
      queryForm.pageNum = 1;
      pagination.value.current = 1;
      await fetchTable();
    } catch (e) {
      message.error(e.data ? e.data.msg : e.message);
    }
  }

  // 查询表格
  const loading = ref(false);
  const tableData = ref([]);
  const pagination = ref({ current: 1, pageSize: 10, total: 0, showSizeChanger: true, showQuickJumper: true, onChange: onPageChange, onShowSizeChange: onSizeChange });

  const queryForm = {
    roleName: '',
    roleCode: '',
    status: undefined,
    parentId: undefined,
    pageNum: 1,
    pageSize: 10,
  };

  function onPageChange(page) {
    queryForm.pageNum = page;
    pagination.value.current = page;
    fetchTable();
  }
  function onSizeChange(_, size) {
    queryForm.pageSize = size;
    pagination.value.pageSize = size;
    fetchTable();
  }

  async function fetchTable() {
    loading.value = true;
    try {
      if (showAll.value) {
        // 展示全部数据：使用分页查询
        const param = {
          roleName: '',
          roleCode: '',
          status: undefined,
          // 不按父ID过滤，确保展示全部
          pageNum: pagination.value.current,
          pageSize: pagination.value.pageSize,
        };
        const res = await sysRoleApi.queryPage(param);
        const page = res.data || {};
        tableData.value = page.list || [];
        pagination.value.total = page.total || 0;
        pagination.value.current = page.pageNum || pagination.value.current || 1;
        pagination.value.pageSize = page.pageSize || pagination.value.pageSize || 10;
      } else {
        // 仅展示当前选中角色
        if (!selectedParentId.value) {
          // 若没有选中项，降级为展示全部
          showAll.value = true;
          const res = await sysRoleApi.queryPage({ pageNum: 1, pageSize: pagination.value.pageSize });
          const page = res.data || {};
          tableData.value = page.list || [];
          pagination.value.total = page.total || 0;
          pagination.value.current = page.pageNum || 1;
          pagination.value.pageSize = page.pageSize || pagination.value.pageSize || 10;
        } else {
          const res = await sysRoleApi.get(selectedParentId.value);
          const row = res.data ? [res.data] : [];
          tableData.value = row;
          pagination.value.total = row.length;
          pagination.value.current = 1;
          // 固定单页展示
          pagination.value.pageSize = Math.max(pagination.value.pageSize, 10);
        }
      }
    } catch (e) {
      message.error(e.data ? e.data.msg : e.message);
    } finally {
      loading.value = false;
    }
  }

  function resetQuery() {
    queryForm.roleName = '';
    queryForm.roleCode = '';
    queryForm.status = undefined;
    queryForm.pageNum = 1;
    pagination.value.current = 1;
    fetchTable();
  }

  // 选择、删除
  const selectedRowKeys = ref([]);
  const rowSelection = {
    selectedRowKeys: selectedRowKeys,
    onChange: (keys) => (selectedRowKeys.value = keys),
  };

  async function onDelete(record) {
    try {
      await sysRoleApi.delete(record.id);
      message.success('删除成功');
      fetchTable();
      refreshLeft();
    } catch (e) {
      message.error(e.data ? e.data.msg : e.message);
    }
  }

  async function onBatchDelete() {
    if (_.isEmpty(selectedRowKeys.value)) return;
    try {
      await sysRoleApi.batchDelete(selectedRowKeys.value);
      message.success('批量删除成功');
      selectedRowKeys.value = [];
      fetchTable();
      refreshLeft();
    } catch (e) {
      message.error(e.data ? e.data.msg : e.message);
    }
  }

  // 详情
  const detailVisible = ref(false);
  const detailData = ref(null);
  async function onDetail(record) {
    try {
      const res = await sysRoleApi.get(record.id);
      detailData.value = res.data || null;
      detailVisible.value = true;
    } catch (e) {
      message.error(e.data ? e.data.msg : e.message);
    }
  }

  // 新增/编辑
  const formVisible = ref(false);
  const formSubmitting = ref(false);
  const formRef = ref();
  const formModel = ref({ id: undefined, roleCode: '', roleName: '', parentId: undefined, orderNum: 0, level: 0, jxfm: '', dy: '', status: 1, remark: '' });
  const formRules = {
    roleCode: [{ required: true, message: '角色标识不能为空' }],
    roleName: [{ required: true, message: '角色名称不能为空' }],
    level: [{ required: true, message: '等级不能为空' }],
    status: [{ required: true, message: '状态不能为空' }],
  };
  const modalTitle = ref('新增角色');

  function onAdd() {
    modalTitle.value = '新增角色';
    formModel.value = { id: undefined, roleCode: '', roleName: '', parentId: selectedParentId.value || 0, orderNum: 0, level: 0, jxfm: '', dy: '', status: 1, remark: '' };
    formVisible.value = true;
  }
  function onEdit(record) {
    modalTitle.value = '编辑角色';
    formModel.value = { id: record.id, roleCode: record.roleCode, roleName: record.roleName, parentId: record.parentId, orderNum: record.orderNum, level: record.level, jxfm: record.jxfm, dy: record.dy, status: record.status, remark: record.remark };
    formVisible.value = true;
  }

  function onCancel() {
    formVisible.value = false;
  }

  async function onSubmit() {
    try {
      await formRef.value.validate();
      formSubmitting.value = true;
      if (formModel.value.id) {
        await sysRoleApi.update(formModel.value);
        message.success('更新成功');
      } else {
        await sysRoleApi.add(formModel.value);
        message.success('新增成功');
      }
      formVisible.value = false;
      fetchTable();
      refreshLeft();
    } catch (e) {
      if (e?.errorFields) return; // 表单校验错误
      message.error(e.data ? e.data.msg : e.message);
    } finally {
      formSubmitting.value = false;
    }
  }

  onMounted(async () => {
    await refreshLeft();
  });
</script>

<style scoped>
  .ant-list-item-selected {
    background-color: #e6f7ff;
  }
</style>