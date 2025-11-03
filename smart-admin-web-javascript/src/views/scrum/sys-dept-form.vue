<!--
  * 部门管理
  *
  * @Author:    oyt
  * @Date:      2025-10-11 17:57:33
  * @Copyright  oyt
-->
<template>
  <a-modal
      :title="form.id ? '编辑' : '添加'"
      :width="800"
      :open="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" >
        <a-form-item label="父部门"  name="parentId">
          <a-tree-select
            v-model:value="form.parentId"
            :treeData="deptTreeOptions"
            :fieldNames="{ label: 'title', value: 'key', children: 'children' }"
            treeDefaultExpandAll
            showSearch
            allowClear
            placeholder="请选择父部门（不选为顶级）"
            style="width: 100%"
            @change="onParentChange"
          />
        </a-form-item>
        <a-form-item label="部门标识（唯一）"  name="deptCode">
          <a-input style="width: 100%" v-model:value="form.deptCode" placeholder="部门标识（唯一）" />
        </a-form-item>
        <a-form-item label="部门等级"  name="level">
          <a-input-number style="width: 100%" v-model:value="form.level" disabled />
        </a-form-item>
        <a-form-item label="部门名称"  name="deptName">
          <a-input style="width: 100%" v-model:value="form.deptName" placeholder="部门名称" />
        </a-form-item>
        <a-form-item label="关联地区"  name="dptarea">
          <a-cascader
            v-model:value="areaSelection"
            :options="areaOptions"
            :loadData="loadAreaChildren"
            placeholder="请选择关联地区（省/市/区）"
            style="width: 100%"
            @change="onAreaChange"
          />
        </a-form-item>
        <a-form-item label="状态"  name="status">
          <a-select style="width: 100%" v-model:value="form.status" placeholder="请选择状态">
            <a-select-option :value="1">正常</a-select-option>
            <a-select-option :value="0">停用</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="备注"  name="remark">
          <a-textarea style="width: 100%" v-model:value="form.remark" :rows="3" placeholder="备注" />
        </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import _ from 'lodash';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { sysDeptApi } from '/@/api/scrum/sys-dept-api';
  import { sysAreaApi } from '/@/api/scrum/sys-area-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  // ------------------------ 事件 ------------------------

  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  // 是否显示
  const visibleFlag = ref(false);

  async function show(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
    await ensureDeptOptions();
    await ensureAreaTopOptions();
    // 根据父部门自动计算等级
    recalcLevel();
    await syncAreaSelectionFromDptarea();
    // 使用字典时把下面这注释修改成自己的字典字段 有多个字典字段就复制多份同理修改 不然打开表单时不显示字典初始值
    // if (form.status && form.status.length > 0) {
    //   form.status = form.status.map((e) => e.valueCode);
    // }
    visibleFlag.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  function onClose() {
    Object.assign(form, formDefault);
    visibleFlag.value = false;
  }

  // ------------------------ 表单 ------------------------

  // 组件ref
  const formRef = ref();

  const formDefault = {
      id: undefined, //部门ID（主键）
      parentId: undefined, //父部门ID（0=顶级）
      deptCode: undefined, //部门标识（唯一）
      deptName: undefined, //部门名称
      orderNum: undefined, //排序（已隐藏，不在编辑界面显示）
      dptarea: undefined, //关联区域ID（逗号分隔）
      status: 1, //状态（1=正常，0=停用）
      remark: undefined, //备注
      level: 1, //部门等级（1=总部，2=分公司，3=部门）
  };

  let form = reactive({ ...formDefault });

  const rules = {
      deptCode: [{ required: true, message: '部门标识（唯一） 必填' }],
      deptName: [{ required: true, message: '部门名称 必填' }],
      status: [{ required: true, message: '状态 必填' }],
  };

  // ------------------------ 父部门选择（构建树） ------------------------
  const deptTreeOptions = ref([]);
  const deptLevelMap = new Map(); // id -> level

  async function ensureDeptOptions() {
    try {
      if (deptTreeOptions.value && deptTreeOptions.value.length > 0) {
        return;
      }
      const res = await sysDeptApi.listAll();
      const list = res.data || [];
      // 构建 map
      deptLevelMap.clear();
      list.forEach((d) => {
        if (d && d.id != null) {
          deptLevelMap.set(String(d.id), Number(d.level || 1));
        }
      });
      // 构树
      deptTreeOptions.value = buildDeptTree(list);
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function buildDeptTree(list) {
    const nodeMap = new Map();
    const roots = [];
    list.forEach((item) => {
      const node = {
        title: `${item.deptName} (${item.deptCode || item.id})`,
        key: String(item.id),
        children: [],
      };
      nodeMap.set(String(item.id), node);
    });
    list.forEach((item) => {
      const pid = item.parentId != null ? String(item.parentId) : '0';
      const node = nodeMap.get(String(item.id));
      const parent = nodeMap.get(pid);
      if (!parent || pid === '0') {
        roots.push(node);
      } else {
        parent.children.push(node);
      }
    });
    return roots;
  }

  function onParentChange() {
    recalcLevel();
  }

  function recalcLevel() {
    if (!form || !form.parentId) {
      form.level = 1;
      return;
    }
    const pLevel = deptLevelMap.get(String(form.parentId));
    if (pLevel != null) {
      const lv = Math.min(Number(pLevel) + 1, 3);
      form.level = lv;
    } else {
      form.level = 1;
    }
  }

  // ------------------------ 关联地区（级联显示层级结构） ------------------------
  const areaOptions = ref([]);
  const areaSelection = ref();

  async function ensureAreaTopOptions() {
    try {
      if (areaOptions.value && areaOptions.value.length > 0) {
        return;
      }
      const res = await sysAreaApi.listTop();
      const tops = (res.data || []).map((a) => ({
        value: String(a.id),
        label: a.name,
        isLeaf: a.level === 3,
      }));
      areaOptions.value = tops;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function loadAreaChildren(selectedOptions) {
    const targetOption = selectedOptions[selectedOptions.length - 1];
    targetOption.loading = true;
    try {
      const res = await sysAreaApi.listChildren(targetOption.value);
      const children = (res.data || []).map((a) => ({
        value: String(a.id),
        label: a.name,
        isLeaf: a.level === 3,
      }));
      // 直辖市回退：顶级无二级时尝试 cityId 的子级
      if (children.length === 0 && String(targetOption.value).length === 6 && String(targetOption.value).endsWith('0000')) {
        const cityId = String(targetOption.value).substring(0, 2) + '0100';
        const res2 = await sysAreaApi.listChildren(cityId);
        const children2 = (res2.data || []).map((a) => ({
          value: String(a.id),
          label: a.name,
          isLeaf: a.level === 3,
        }));
        targetOption.children = children2;
      } else {
        targetOption.children = children;
      }
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      targetOption.loading = false;
    }
  }

  // 使用 a-cascader 默认的显示策略（自动拼接层级文本），无需自定义

  function onAreaChange(values) {
    if (values && values.length > 0) {
      form.dptarea = String(values[values.length - 1]);
    } else {
      form.dptarea = undefined;
    }
  }

  // 根据已有 dptarea 预选级联路径，保证编辑时显示层级文本
  async function syncAreaSelectionFromDptarea() {
    try {
      areaSelection.value = undefined;
      const targetId = form && form.dptarea ? String(form.dptarea) : undefined;
      if (!targetId || targetId.length < 6) {
        return;
      }
      const provId = targetId.substring(0, 2) + '0000';
      let cityId = targetId.substring(0, 4) + '00';

      // 找省份
      const provOpt = (areaOptions.value || []).find((o) => o.value === provId);
      if (!provOpt) {
        return;
      }
      // 加载省的子级（城市）
      if (!provOpt.children || provOpt.children.length === 0) {
        await loadAreaChildren([provOpt]);
      }

      // 找城市，支持直辖市“市辖区”回退
      let cityOpt = (provOpt.children || []).find((c) => c.value === cityId);
      const isMunicipality = ['11', '12', '31', '50'].includes(provId.substring(0, 2));
      if (!cityOpt) {
        // 如果省下面没有市级，尝试创建“市辖区”占位并继续加载区县
        if (isMunicipality) {
          cityOpt = { value: cityId, label: '市辖区', isLeaf: false };
          provOpt.children = provOpt.children || [];
          provOpt.children.push(cityOpt);
        } else {
          // 非直辖市数据不完整时也尝试占位，避免不显示
          cityOpt = { value: cityId, label: '', isLeaf: false };
          provOpt.children = provOpt.children || [];
          provOpt.children.push(cityOpt);
        }
      }

      // 加载城市的子级（区县）
      if (!cityOpt.children || cityOpt.children.length === 0) {
        await loadAreaChildren([provOpt, cityOpt]);
      }

      // 设置选中路径（省-市-区），让级联显示层级文本
      areaSelection.value = [provId, cityId, targetId];
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  // 点击确定，验证表单
  async function onSubmit() {
    try {
      await formRef.value.validateFields();
      save();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  // 新建、编辑API
  async function save() {
    SmartLoading.show();
    try {
      if (form.id) {
        await sysDeptApi.update(form);
      } else {
        await sysDeptApi.add(form);
      }
      message.success('操作成功');
      emits('reloadList');
      onClose();
    } catch (err) {
      smartSentry.captureError(err);
    } finally {
      SmartLoading.hide();
    }
  }

  defineExpose({
    show,
  });
</script>
<style scoped>
</style>
