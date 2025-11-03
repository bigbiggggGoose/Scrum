<!--
  * 站内信/邮件模板表
  *
  * @Author:    thr
  * @Date:      2025-10-12 14:33:45
  * @Copyright  wu
-->
<template>
  <a-drawer
      :title="form.id ? '编辑' : '添加'"
      :width="800"
      :open="visibleFlag"
      @close="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" >
        <a-form-item label="主键"  name="id">
          <a-input-number style="width: 100%" v-model:value="form.id" placeholder="主键" />
        </a-form-item>
        <a-form-item label="模板标识（唯一，如sprint_start_notice）"  name="templateCode">
          <a-input style="width: 100%" v-model:value="form.templateCode" placeholder="模板标识（唯一，如sprint_start_notice）" />
        </a-form-item>
        <a-form-item label="类型（1=站内信，2=邮件）"  name="templateType">
          <a-input-number style="width: 100%" v-model:value="form.templateType" placeholder="类型（1=站内信，2=邮件）" />
        </a-form-item>
        <a-form-item label="模板名称（如Sprint启动通知模板）"  name="templateName">
          <a-input style="width: 100%" v-model:value="form.templateName" placeholder="模板名称（如Sprint启动通知模板）" />
        </a-form-item>
        <a-form-item label="模板内容（支持变量占位符，如${sprintName}）"  name="templateContent">
          <a-textarea style="width: 100%" v-model:value="form.templateContent" placeholder="模板内容（支持变量占位符，如${sprintName}）" />
        </a-form-item>
        <a-form-item label="创建时间"  name="createTime">
          <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.createTime" style="width: 100%" placeholder="创建时间"/>
        </a-form-item>
        <a-form-item label="更新时间"  name="updateTime">
          <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.updateTime" style="width: 100%" placeholder="更新时间"/>
        </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-drawer>
</template>
<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import _ from 'lodash';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { templateApi } from '/@/api/scrum/template-api';
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
      id: undefined, //主键
      templateCode: undefined, //模板标识（唯一，如sprint_start_notice）
      templateType: undefined, //类型（1=站内信，2=邮件）
      templateName: undefined, //模板名称（如Sprint启动通知模板）
      templateContent: undefined, //模板内容（支持变量占位符，如${sprintName}）
      createTime: undefined, //创建时间
      updateTime: undefined, //更新时间
  };

  let form = reactive({ ...formDefault });

  const rules = {
      id: [{ required: true, message: '主键 必填' }],
      templateCode: [{ required: true, message: '模板标识（唯一，如sprint_start_notice） 必填' }],
      templateType: [{ required: true, message: '类型（1=站内信，2=邮件） 必填' }],
      templateName: [{ required: true, message: '模板名称（如Sprint启动通知模板） 必填' }],
      templateContent: [{ required: true, message: '模板内容（支持变量占位符，如${sprintName}） 必填' }],
      createTime: [{ required: true, message: '创建时间 必填' }],
      updateTime: [{ required: true, message: '更新时间 必填' }],
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
        await templateApi.update(form);
      } else {
        await templateApi.add(form);
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
