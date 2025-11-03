<!--
  * 产品表
  *
  * @Author:    cmz
  * @Date:      2025-10-11 22:39:41
  * @Copyright  1
-->
<template>
  <a-drawer
      :title="form.id ? '编辑' : '添加'"
      :width="900"
      :open="visibleFlag"
      @close="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-row :gutter="16">
        <a-col :span="8">
          <a-form-item label="产品名称" name="productName">
            <a-input v-model:value="form.productName" placeholder="产品名称" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="产品编号" name="productCode">
            <a-input v-model:value="form.productCode" placeholder="产品编号" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="产品来源" name="productSource">
            <a-select v-model:value="form.productSource" placeholder="请选择">
              <a-select-option value="客户需求">客户需求</a-select-option>
              <a-select-option value="内部立项">内部立项</a-select-option>
              <a-select-option value="其他">其他</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="8">
          <a-form-item label="状态" name="productStatus">
            <a-select v-model:value="form.productStatus" placeholder="请选择">
              <a-select-option value="1">未开始</a-select-option>
              <a-select-option value="2">进行中</a-select-option>
              <a-select-option value="3">未完成</a-select-option>
              <a-select-option value="4">已完成</a-select-option>
              <a-select-option value="5">已关闭</a-select-option>
              <a-select-option value="6">已挂起</a-select-option>
              <a-select-option value="7">已延期</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="PO" name="productOwnerId">
            <a-select v-model:value="form.productOwnerId" :options="poOptions" placeholder="请选择PO" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="总人日(天)" name="totalManDay">
            <a-input-number v-model:value="form.totalManDay" style="width:100%" :step="0.5" placeholder="总人日(天)" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <!-- 移除 Scrum Master 与 团队ID 输入项 -->
        <a-col :span="8">
          <a-form-item label="产品价值" name="productValue">
            <a-input-number v-model:value="form.productValue" style="width:100%" placeholder="产品价值" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="产品进度" name="productProgress">
            <a-slider v-model:value="form.productProgress" :min="0" :max="100" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="开始时间" name="planStartDate">
            <a-date-picker v-model:value="form.planStartDate" style="width:100%" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="结束时间" name="planEndDate">
            <a-date-picker v-model:value="form.planEndDate" style="width:100%" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="干系人" name="stakeholders">
            <a-textarea v-model:value="form.stakeholders" :rows="2" placeholder="干系人" />
           </a-form-item>
         </a-col>
         <a-col :span="12">
           <a-form-item label="用户和客户" name="customers">
             <a-input v-model:value="form.customers" placeholder="用户和客户" />
           </a-form-item>
         </a-col>
       </a-row>
 
       <a-row :gutter="16">
         <a-col :span="24">
           <a-form-item label="产品备注" name="productRemarks">
            <a-textarea v-model:value="form.productRemarks" :rows="4" placeholder="产品备注" />
           </a-form-item>
         </a-col>
       </a-row>
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
  import { scrumProductsApi } from '/@/api/business/scrum-products/scrum-products-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import dayjs from 'dayjs';
  import { sysUserApi } from '/@/api/scrum/sys-user-api';

  const emits = defineEmits(['reloadList']);

  const visibleFlag = ref(false);
  const poOptions = ref([]);

  async function loadPoOptions() {
    try {
      const res = await sysUserApi.queryPage({ pageNum: 1, pageSize: 200 });
      const list = res?.data?.list || [];
      poOptions.value = list.map((u) => ({ label: u.realName, value: u.id }));
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function show(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
      // normalize date values to Dayjs for DatePicker
      form.planStartDate = form.planStartDate ? dayjs(form.planStartDate) : undefined;
      form.planEndDate = form.planEndDate ? dayjs(form.planEndDate) : undefined;
      // normalize numeric values to Number for InputNumber/Slider
      form.productOwnerId = form.productOwnerId != null ? Number(form.productOwnerId) : undefined;
      form.productProgress = form.productProgress != null ? Number(form.productProgress) : 0;
      form.totalManDay = form.totalManDay != null ? Number(form.totalManDay) : undefined;
      form.productValue = form.productValue != null ? Number(form.productValue) : undefined;
    }
    visibleFlag.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
    loadPoOptions();
  }

  function onClose() {
    Object.assign(form, formDefault);
    visibleFlag.value = false;
  }

  const formRef = ref();

  const formDefault = {
    id: undefined,
    productName: undefined,
    productCode: undefined,
    productSource: undefined,
    productStatus: undefined,
    productOwnerId: undefined,
    productProgress: 0,
    totalManDay: undefined,
    productValue: undefined,
    planStartDate: undefined,
    planEndDate: undefined,
    stakeholders: undefined,
    customers: undefined,
    productRemarks: undefined,
  };

  let form = reactive({ ...formDefault });

  const rules = {
    productName: [{ required: true, message: '产品名称 必填' }],
    productCode: [{ required: true, message: '产品编号 必填' }],
    productOwnerId: [{ required: true, message: 'PO 必填' }],
    productStatus: [{ required: true, message: '状态 必填' }],
    productProgress: [{ required: true, message: '产品进度 必填' }],
  };

  async function onSubmit() {
    try {
      await formRef.value.validateFields();
      save();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  async function save() {
    SmartLoading.show();
    try {
      const payload = {
        ...form,
        planStartDate: form.planStartDate ? form.planStartDate.format('YYYY-MM-DD') : undefined,
        planEndDate: form.planEndDate ? form.planEndDate.format('YYYY-MM-DD') : undefined,
      };
      if (form.id) {
        await scrumProductsApi.update(payload);
      } else {
        await scrumProductsApi.add(payload);
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
