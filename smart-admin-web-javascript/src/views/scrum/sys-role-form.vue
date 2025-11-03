<!--
  * 角色管理
  *
  * @Author:    oyt
  * @Date:      2025-10-11 18:33:10
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
        <a-form-item label="角色ID（主键）"  name="id">
          <a-input-number style="width: 100%" v-model:value="form.id" placeholder="角色ID（主键）" />
        </a-form-item>
        <a-form-item label="角色标识（唯一，如ROLE_ADMIN）"  name="roleCode">
          <a-input style="width: 100%" v-model:value="form.roleCode" placeholder="角色标识（唯一，如ROLE_ADMIN）" />
        </a-form-item>
        <a-form-item label="角色名称（如超级管理员）"  name="roleName">
          <a-input style="width: 100%" v-model:value="form.roleName" placeholder="角色名称（如超级管理员）" />
        </a-form-item>
        <a-form-item label="角色等级"  name="level">
          <a-input-number style="width: 100%" v-model:value="form.level" placeholder="角色等级" />
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
  import { sysRoleApi } from '/@/api/scrum/sys-role-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  // ------------------------ 事件 ------------------------

  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  // 是否显示
  const visibleFlag = ref(false);

  function show(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
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
      id: undefined, //角色ID（主键）
      roleCode: undefined, //角色标识（唯一，如ROLE_ADMIN）
      roleName: undefined, //角色名称（如超级管理员）
      level: undefined, //角色等级
  };

  let form = reactive({ ...formDefault });

  const rules = {
      id: [{ required: true, message: '角色ID（主键） 必填' }],
      roleCode: [{ required: true, message: '角色标识（唯一，如ROLE_ADMIN） 必填' }],
      roleName: [{ required: true, message: '角色名称（如超级管理员） 必填' }],
      level: [{ required: true, message: '角色等级 必填' }],
  };

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
        await sysRoleApi.update(form);
      } else {
        await sysRoleApi.add(form);
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
