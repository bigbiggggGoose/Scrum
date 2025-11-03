<!--
  * 系统动态表（全局操作记录）
  *
  * @Author:    a
  * @Date:      2025-09-30 10:32:40
  * @Copyright  a
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
        <a-form-item label="动态ID（主键）"  name="id">
          <a-input-number style="width: 100%" v-model:value="form.id" placeholder="动态ID（主键）" />
        </a-form-item>
        <a-form-item label="操作用户ID（关联sys_user.id）"  name="userId">
          <a-input-number style="width: 100%" v-model:value="form.userId" placeholder="操作用户ID（关联sys_user.id）" />
        </a-form-item>
        <a-form-item label="用户名（冗余，便于前端显示）"  name="userName">
          <a-input style="width: 100%" v-model:value="form.userName" placeholder="用户名（冗余，便于前端显示）" />
        </a-form-item>
        <a-form-item label="操作类型（add=添加，delete=删除，update=修改，view=查看）"  name="actionType">
          <a-input style="width: 100%" v-model:value="form.actionType" placeholder="操作类型（add=添加，delete=删除，update=修改，view=查看）" />
        </a-form-item>
        <a-form-item label="操作对象类型（product=产品，sprint=Sprint，sprint_backlog=Sprint待办）"  name="targetType">
          <a-input style="width: 100%" v-model:value="form.targetType" placeholder="操作对象类型（product=产品，sprint=Sprint，sprint_backlog=Sprint待办）" />
        </a-form-item>
        <a-form-item label="操作对象ID"  name="targetId">
          <a-input-number style="width: 100%" v-model:value="form.targetId" placeholder="操作对象ID" />
        </a-form-item>
        <a-form-item label="动态内容（如"Rick添加Sprint待办：9.23考试""李华删除产品：XX系统"）"  name="activityContent">
          <a-input style="width: 100%" v-model:value="form.activityContent" placeholder="动态内容（如"Rick添加Sprint待办：9.23考试""李华删除产品：XX系统"）" />
        </a-form-item>
        <a-form-item label="操作时间"  name="createTime">
          <a-date-picker show-time valueFormat="YYYY-MM-DD HH:mm:ss" v-model:value="form.createTime" style="width: 100%" placeholder="操作时间" />
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
  import { sysActivitiesApi } from '/@/api/business/scrum/sys-activities/sys-activities-api';
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
      id: undefined, //动态ID（主键）
      userId: undefined, //操作用户ID（关联sys_user.id）
      userName: undefined, //用户名（冗余，便于前端显示）
      actionType: undefined, //操作类型（add=添加，delete=删除，update=修改，view=查看）
      targetType: undefined, //操作对象类型（product=产品，sprint=Sprint，sprint_backlog=Sprint待办）
      targetId: undefined, //操作对象ID
      activityContent: undefined, //动态内容（如"Rick添加Sprint待办：9.23考试""李华删除产品：XX系统"）
      createTime: undefined, //操作时间
  };

  let form = reactive({ ...formDefault });

  const rules = {
      id: [{ required: true, message: '动态ID（主键） 必填' }],
      userId: [{ required: true, message: '操作用户ID（关联sys_user.id） 必填' }],
      userName: [{ required: true, message: '用户名（冗余，便于前端显示） 必填' }],
      actionType: [{ required: true, message: '操作类型（add=添加，delete=删除，update=修改，view=查看） 必填' }],
      targetType: [{ required: true, message: '操作对象类型（product=产品，sprint=Sprint，sprint_backlog=Sprint待办） 必填' }],
      targetId: [{ required: true, message: '操作对象ID 必填' }],
      activityContent: [{ required: true, message: '动态内容（如"Rick添加Sprint待办：9.23考试""李华删除产品：XX系统"） 必填' }],
      createTime: [{ required: true, message: '操作时间 必填' }],
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
        await sysActivitiesApi.update(form);
      } else {
        await sysActivitiesApi.add(form);
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
