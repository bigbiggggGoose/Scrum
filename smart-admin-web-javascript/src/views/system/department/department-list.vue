<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="部门名称" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="keywords" placeholder="请输入部门名称" />
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button-group>
          <a-button v-privilege="'support:department:query'" type="primary" @click="onSearch">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
          <a-button v-privilege="'support:department:query'" @click="resetQuery">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-button-group>
        <a-button v-privilege="'system:department:add'" type="primary" @click="addDepartment" class="smart-margin-left20">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="true">
    <a-table
      size="small"
      bordered
      :loading="tableLoading"
      rowKey="departmentId"
      :columns="columns"
      :data-source="departmentTreeData"
      :defaultExpandAllRows="false"
      :defaultExpandedRowKeys="defaultExpandedRowList"
      :pagination="false"
    >
      <template #bodyCell="{ record, column }">
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addDepartment(record)" v-privilege="'system:department:add'" type="link">添加下级</a-button>
            <a-button @click="updateDepartment(record)" v-privilege="'system:department:update'" type="link">编辑</a-button>
            <a-button
              danger
              v-if="record.departmentId !== topDepartmentId"
              v-privilege="'system:department:delete'"
              @click="deleteDepartment(record.departmentId)"
              type="link"
              >删除</a-button
            >
          </div>
        </template>
      </template>
    </a-table>
    <!-- 添加编辑部门弹窗 -->
    <DepartmentFormModal ref="departmentFormModal" @refresh="onFormRefresh" />
  </a-card>
</template>

<script setup>
  import { onMounted, reactive, ref, watch, createVNode } from 'vue';
  import { departmentApi } from '/@/api/system/department-api';
  import { Modal, message } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import _ from 'lodash';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import DepartmentFormModal from './components/department-form-modal.vue';
  import { smartSentry } from '/@/lib/smart-sentry';

  const DEPARTMENT_PARENT_ID = 0;

  // -----------------------  筛选 ---------------------
  const keywords = ref('');

  // ----------------------- 部门树的展示 ---------------------
  const tableLoading = ref(false);

  const topDepartmentId = ref();
  // 所有部门列表
  const departmentList = ref([]);
  // 部门树形数据
  const departmentTreeData = ref([]);
  // 存放部门id和部门，用于查找
  const idInfoMap = ref(new Map());
  // 默认展开的行
  const defaultExpandedRowList = reactive([]);

  const columns = ref([
    {
      title: '部门名称',
      dataIndex: 'departmentName',
      key: 'departmentName',
    },
    {
      title: '负责人',
      dataIndex: 'managerName',
      key: 'managerName',
      width: 100,
    },
    {
      title: '排序',
      dataIndex: 'sort',
      key: 'sort',
      width: 100,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 150,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 200,
    },
  ]);

  onMounted(() => {
    queryDepartmentTree();
  });

  // 查询部门列表并构建 部门树
  async function queryDepartmentTree() {
    try {
      tableLoading.value = true;
      let res = await departmentApi.queryAllDepartment();
      let data = res.data;

      data.forEach((e) => {
        idInfoMap.value.set(e.departmentId, e);
      });

      departmentList.value = data;
      departmentTreeData.value = buildDepartmentTreeLinear(data, DEPARTMENT_PARENT_ID);

      // 默认显示 最顶级ID为列表中返回的第一条数据的ID
      if (!_.isEmpty(departmentTreeData.value) && departmentTreeData.value.length > 0) {
        topDepartmentId.value = departmentTreeData.value[0].departmentId;
      }

      defaultExpandedRowList.value = [];
      defaultExpandedRowList.push(topDepartmentId.value);
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  // 构建部门树（O(n) 实现）
  function buildDepartmentTreeLinear(data, rootParentId) {
    if (!data || data.length === 0) {
      return null;
    }
    const nodeMap = new Map();
    // 初始化节点 children
    data.forEach((item) => {
      item.children = [];
      nodeMap.set(item.departmentId, item);
    });
    const roots = [];
    data.forEach((item) => {
      const pId = item.parentId;
      if (pId === rootParentId || !nodeMap.has(pId)) {
        roots.push(item);
      } else {
        const parent = nodeMap.get(pId);
        parent.children.push(item);
      }
    });
    // 按 sort 倒序，保持与后端 listAll 排序一致
    const sortDesc = (a, b) => (b.sort || 0) - (a.sort || 0);
    const sortTree = (list) => {
      if (!list) return;
      list.sort(sortDesc);
      list.forEach((n) => sortTree(n.children));
    };
    sortTree(roots);
    return roots.length ? roots : null;
  }

  // 重置
  function resetQuery() {
    keywords.value = '';
    onSearch();
  }

  // 搜索
  function onSearch() {
    if (!keywords.value) {
      departmentTreeData.value = buildDepartmentTreeLinear(departmentList.value, DEPARTMENT_PARENT_ID);
      return;
    }
    let originData = departmentList.value.concat();
    if (!originData) {
      return;
    }
    // 筛选出名称符合的部门
    let filterDepartment = originData.filter((e) => e.departmentName.indexOf(keywords.value) > -1);
    let filterDepartmentList = [];
    // 循环筛选出的部门 构建部门树
    filterDepartment.forEach((e) => {
      recursionFilterDepartment(filterDepartmentList, e.departmentId, false);
    });
    departmentTreeData.value = buildDepartmentTreeLinear(filterDepartmentList, DEPARTMENT_PARENT_ID);
  }

  // 根据ID递归筛选部门
  function recursionFilterDepartment(resList, id, unshift) {
    let info = idInfoMap.value.get(id);
    if (!info || resList.some((e) => e.departmentId === id)) {
      return;
    }
    if (unshift) {
      resList.unshift(info);
    } else {
      resList.push(info);
    }
    if (info.parentId && info.parentId !== 0) {
      recursionFilterDepartment(resList, info.parentId, unshift);
    }
  }

  // ----------------------- 表单操作：添加部门/修改部门/删除部门/上下移动 ---------------------
  const departmentFormModal = ref();
  // 添加
  function addDepartment(e) {
    let data = {
      departmentId: 0,
      departmentName: '',
      parentId: e.departmentId || null,
    };
    departmentFormModal.value.showModal(data);
  }
  // 编辑
  function updateDepartment(e) {
    departmentFormModal.value.showModal(e);
  }

  // ----------------------- 增量插入 & 保持展开状态 ---------------------
  function onFormRefresh(newDept) {
    // 如果携带新增部门对象，做增量插入；否则执行全量刷新（用于编辑场景）
    if (newDept && newDept.departmentId) {
      incrementalInsert(newDept);
      // 如存在关键词筛选，按现有逻辑重新构建筛选树
      if (keywords.value) {
        onSearch();
      }
    } else {
      queryDepartmentTree();
    }
  }

  function incrementalInsert(newDept) {
    // 更新扁平列表与索引映射
    departmentList.value.push(newDept);
    idInfoMap.value.set(newDept.departmentId, newDept);

    // 在现有树中定位父节点并插入
    const parentId = newDept.parentId ?? DEPARTMENT_PARENT_ID;
    if (parentId === DEPARTMENT_PARENT_ID) {
      insertIntoChildren(departmentTreeData.value, newDept);
      return;
    }
    const parentNode = findNodeById(departmentTreeData.value, parentId);
    if (parentNode) {
      if (!parentNode.children) {
        parentNode.children = [];
      }
      insertIntoChildren(parentNode.children, newDept);
    } else {
      // 若父节点不在当前树（极端情况），回退到全量重建，但不发起网络请求
      departmentTreeData.value = buildDepartmentTreeLinear(departmentList.value, DEPARTMENT_PARENT_ID);
    }
  }

  function findNodeById(list, id) {
    if (!list || list.length === 0) return null;
    for (const node of list) {
      if (node.departmentId === id) return node;
      const found = findNodeById(node.children, id);
      if (found) return found;
    }
    return null;
  }

  function insertIntoChildren(children, node) {
    if (!children) return;
    // 依据 sort 值倒序插入，保持展示顺序
    let inserted = false;
    for (let i = 0; i < children.length; i++) {
      const cur = children[i];
      const curSort = cur.sort || 0;
      const newSort = node.sort || 0;
      if (newSort > curSort) {
        children.splice(i, 0, node);
        inserted = true;
        break;
      }
    }
    if (!inserted) {
      children.push(node);
    }
  }

  // 删除
  function deleteDepartment(id) {
    Modal.confirm({
      title: '提醒',
      icon: createVNode(ExclamationCircleOutlined),
      content: '确定要删除该部门吗?',
      okText: '删除',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await departmentApi.deleteDepartment(id);
          // 增量移除：从树和列表中删除该节点
          removeNodeById(departmentTreeData.value, id);
          departmentList.value = departmentList.value.filter((e) => e.departmentId !== id);
          idInfoMap.value.delete(id);
          message.success('删除成功');
          // 如存在关键词筛选，按现有逻辑重新构建筛选树
          if (keywords.value) {
            onSearch();
          }
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  // 递归从树中删除指定 id 的节点（叶子删除）
  function removeNodeById(list, targetId) {
    if (!list || list.length === 0) return false;
    for (let i = 0; i < list.length; i++) {
      const node = list[i];
      if (node.departmentId === targetId) {
        list.splice(i, 1);
        return true;
      }
      if (removeNodeById(node.children, targetId)) {
        return true;
      }
    }
    return false;
  }
</script>

<style scoped lang="less"></style>
