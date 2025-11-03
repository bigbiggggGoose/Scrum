<!--
  * 项目迭代表
  *
  * @Author:    cmz
  * @Date:      2025-10-11 22:41:18
  * @Copyright  1
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
        <a-form-item label="Sprint ID（主键）"  name="id">
          <a-input-number style="width: 100%" v-model:value="form.id" placeholder="Sprint ID（主键）" />
        </a-form-item>
        <a-form-item label="Sprint名称（如数据库构建、系统设计）"  name="sprintName">
          <a-input style="width: 100%" v-model:value="form.sprintName" placeholder="Sprint名称（如数据库构建、系统设计）" />
        </a-form-item>
        <a-form-item label="所属产品ID（关联scrum_products.id）"  name="productId">
          <a-input-number style="width: 100%" v-model:value="form.productId" placeholder="所属产品ID（关联scrum_products.id）" />
        </a-form-item>
        <a-form-item label="负责团队ID（关联scrum_teams.id）"  name="teamId">
          <a-input-number style="width: 100%" v-model:value="form.teamId" placeholder="负责团队ID（关联scrum_teams.id）" />
        </a-form-item>
        <a-form-item label="开始时间"  name="startDate">
          <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.startDate" style="width: 100%" placeholder="开始时间"/>
        </a-form-item>
        <a-form-item label="结束时间"  name="endDate">
          <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.endDate" style="width: 100%" placeholder="结束时间"/>
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
  import { scrumSprintsApi } from '/@/api/business/scrum-sprints/scrum-sprints-api';
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
      id: undefined, //Sprint ID（主键）
      sprintName: undefined, //Sprint名称（如数据库构建、系统设计）
      productId: undefined, //所属产品ID（关联scrum_products.id）
      teamId: undefined, //负责团队ID（关联scrum_teams.id）
      startDate: undefined, //开始时间
      endDate: undefined, //结束时间
  };

  let form = reactive({ ...formDefault });

  const rules = {
      id: [{ required: true, message: 'Sprint ID（主键） 必填' }],
      sprintName: [{ required: true, message: 'Sprint名称（如数据库构建、系统设计） 必填' }],
      productId: [{ required: true, message: '所属产品ID（关联scrum_products.id） 必填' }],
      teamId: [{ required: true, message: '负责团队ID（关联scrum_teams.id） 必填' }],
      startDate: [{ required: true, message: '开始时间 必填' }],
      endDate: [{ required: true, message: '结束时间 必填' }],
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
        await scrumSprintsApi.update(form);
      } else {
        await scrumSprintsApi.add(form);
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
