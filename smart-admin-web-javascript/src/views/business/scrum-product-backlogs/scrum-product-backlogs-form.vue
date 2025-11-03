<!--
  * 产品待办事项表
  *
  * @Author:    cmz
  * @Date:      2025-10-11 22:40:46
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
        <a-form-item label="待办ID（主键）"  name="id">
          <a-input-number style="width: 100%" v-model:value="form.id" placeholder="待办ID（主键）" />
        </a-form-item>
        <a-form-item label="所属产品ID（关联scrum_products.id）"  name="productId">
          <a-input-number style="width: 100%" v-model:value="form.productId" placeholder="所属产品ID（关联scrum_products.id）" />
        </a-form-item>
        <a-form-item label="待办名称（用户故事标题）"  name="backlogName">
          <a-input style="width: 100%" v-model:value="form.backlogName" placeholder="待办名称（用户故事标题）" />
        </a-form-item>
        <a-form-item label="优先级（数字越大优先级越低）"  name="priority">
          <a-input-number style="width: 100%" v-model:value="form.priority" placeholder="优先级（数字越大优先级越低）" />
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
  import { scrumProductBacklogsApi } from '/@/api/business/scrum-product-backlogs/scrum-product-backlogs-api';
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
      id: undefined, //待办ID（主键）
      productId: undefined, //所属产品ID（关联scrum_products.id）
      backlogName: undefined, //待办名称（用户故事标题）
      priority: undefined, //优先级（数字越大优先级越低）
  };

  let form = reactive({ ...formDefault });

  const rules = {
      id: [{ required: true, message: '待办ID（主键） 必填' }],
      productId: [{ required: true, message: '所属产品ID（关联scrum_products.id） 必填' }],
      backlogName: [{ required: true, message: '待办名称（用户故事标题） 必填' }],
      priority: [{ required: true, message: '优先级（数字越大优先级越低） 必填' }],
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
        await scrumProductBacklogsApi.update(form);
      } else {
        await scrumProductBacklogsApi.add(form);
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
