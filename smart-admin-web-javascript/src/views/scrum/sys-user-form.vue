<!--
  * 用户信息表
  *
  * @Author:    oyt
  * @Date:      2025-10-11 17:34:30
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
      centered
      :bodyStyle="{ overflowY: 'visible' }"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" >
        <!-- 隐藏用户ID，仅用于区分新增/编辑，不允许编辑 -->
        <a-form-item style="display:none" name="id">
          <a-input-number v-model:value="form.id" />
        </a-form-item>

        <a-form-item label="登录名"  name="username">
          <a-input style="width: 100%" v-model:value="form.username" placeholder="请输入登录名" />
        </a-form-item>
        <a-form-item label="密码"  name="password">
          <a-input style="width: 100%" v-model:value="form.password" placeholder="请输入密码" type="password" />
        </a-form-item>
        <a-form-item label="真实姓名"  name="realName">
          <a-input style="width: 100%" v-model:value="form.realName" placeholder="请输入真实姓名" />
        </a-form-item>

        <a-form-item label="工资表顺序"  name="sidx">
          <a-input-number style="width: 100%" v-model:value="form.sidx" placeholder="请输入工资表顺序" />
        </a-form-item>
        <a-form-item label="生日"  name="birth">
          <a-date-picker style="width: 100%" v-model:value="form.birth" valueFormat="YYYY-MM-DD" placeholder="请选择日期" />
        </a-form-item>
        <a-form-item label="性别"  name="sex">
          <a-select style="width: 100%" v-model:value="form.sex" placeholder="请选择性别">
            <a-select-option :value="1">男</a-select-option>
            <a-select-option :value="2">女</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="邮箱"  name="email">
          <a-input style="width: 100%" v-model:value="form.email" placeholder="请输入邮箱" />
        </a-form-item>
        <a-form-item label="手机号"  name="phone">
          <a-input style="width: 100%" v-model:value="form.phone" placeholder="请输入手机号" />
        </a-form-item>

        <a-form-item label="角色ID"  name="roleId">
          <a-input-number style="width: 100%" v-model:value="form.roleId" placeholder="请输入角色ID" />
        </a-form-item>
        <a-form-item label="部门ID"  name="deptId">
          <a-input-number style="width: 100%" v-model:value="form.deptId" placeholder="请输入部门ID" />
        </a-form-item>

        <a-form-item label="是否禁用"  name="isDisable">
          <a-select style="width: 100%" v-model:value="form.isDisable" placeholder="请选择是否禁用">
            <a-select-option :value="1">是</a-select-option>
            <a-select-option :value="2">否</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态"  name="status">
          <a-select style="width: 100%" v-model:value="form.status" placeholder="请选择状态">
            <a-select-option :value="1">在职</a-select-option>
            <a-select-option :value="0">离职</a-select-option>
          </a-select>
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
  import { sysUserApi } from '/@/api/scrum/sys-user-api';
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
      id: undefined, // 用于区分新增/编辑，不显示在表单
      username: undefined, // 登录名（唯一）
      password: undefined, // 密码（加密存储，如BCrypt）
      realName: undefined, // 真实姓名
      sidx: undefined, // 工资表顺序
      birth: undefined, // 生日，字符串：YYYY-MM-DD
      sex: undefined, // 性别（1=男，2=女）
      email: undefined, // 邮箱
      phone: undefined, // 手机号（唯一）
      roleId: undefined, // 角色ID
      deptId: undefined, // 部门ID
      isDisable: undefined, // 是否禁用（1=是，2=否）
      status: undefined, // 状态（1=在职，0=离职）
  };

  let form = reactive({ ...formDefault });

  const rules = {
      username: [{ required: true, message: '登录名（唯一） 必填' }],
      password: [{ required: true, message: '密码（加密存储，如BCrypt） 必填' }],
      realName: [{ required: true, message: '真实姓名 必填' }],
      phone: [{ required: true, message: '手机号（唯一） 必填' }],
      isDisable: [{ required: true, message: '是否禁用（1=是，2=否） 必填' }],
      // 其他字段可选按需填写
  };

  // 点击确定，验证表单
  async function onSubmit() {
    try {
      await formRef.value.validate();
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
        await sysUserApi.update(form);
      } else {
        await sysUserApi.add(form);
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
