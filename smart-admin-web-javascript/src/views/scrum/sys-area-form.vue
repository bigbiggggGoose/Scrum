<!--
  * 地区表（省/市/区三级联动）
  *
  * @Author:    oyt
  * @Date:      2025-10-11 13:54:05
  * @Copyright  oyt
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
        <a-form-item label="区域编码"  name="id">
          <a-input style="width: 100%" v-model:value="form.id" placeholder="区域编码" />
        </a-form-item>
        <a-form-item label="区域名称"  name="name">
          <a-input style="width: 100%" v-model:value="form.name" placeholder="区域名称" />
        </a-form-item>
        <a-form-item label="区域上级标识"  name="pid">
          <a-input style="width: 100%" v-model:value="form.pid" placeholder="区域上级标识" />
        </a-form-item>
        <a-form-item label="地名简称"  name="simplename">
          <a-input style="width: 100%" v-model:value="form.simplename" placeholder="地名简称" />
        </a-form-item>
        <a-form-item label="区域等级"  name="level">
          <a-input-number style="width: 100%" v-model:value="form.level" placeholder="区域等级" />
        </a-form-item>
        <a-form-item label="城市编码"  name="citycode">
          <a-input style="width: 100%" v-model:value="form.citycode" placeholder="城市编码" />
        </a-form-item>
        <a-form-item label="邮政编码"  name="zipcode">
          <a-input style="width: 100%" v-model:value="form.zipcode" placeholder="邮政编码" />
        </a-form-item>
        <a-form-item label="组合名称"  name="mername">
          <a-input style="width: 100%" v-model:value="form.mername" placeholder="组合名称" />
        </a-form-item>
        <a-form-item label="经度"  name="lng">
          <a-input-number style="width: 100%" v-model:value="form.lng" placeholder="经度" />
        </a-form-item>
        <a-form-item label="纬度"  name="lat">
          <a-input-number style="width: 100%" v-model:value="form.lat" placeholder="纬度" />
        </a-form-item>
        <a-form-item label="区域拼音"  name="pinyin">
          <a-input style="width: 100%" v-model:value="form.pinyin" placeholder="区域拼音" />
        </a-form-item>
        <a-form-item label="创建时间"  name="createTime">
          <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.createTime" style="width: 100%" placeholder="创建时间"/>
        </a-form-item>
        <a-form-item label="修改时间"  name="updateTime">
          <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.updateTime" style="width: 100%" placeholder="修改时间"/>
        </a-form-item>
        <a-form-item label="创建人"  name="creator">
          <a-input-number style="width: 100%" v-model:value="form.creator" placeholder="创建人" />
        </a-form-item>
        <a-form-item label="修改人"  name="updater">
          <a-input-number style="width: 100%" v-model:value="form.updater" placeholder="修改人" />
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
  import { sysAreaApi } from '/@/api/scrum/sys-area-api';
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
      id: undefined, //区域编码（主键）
      name: undefined, //区域名称（如北京市、朝阳区）
      pid: undefined, //上级区域ID（0=顶级区域）
      simplename: undefined, //区域简称（如北京、朝阳）
      level: undefined, //区域等级（1=省/直辖市，2=市，3=区/县）
      citycode: undefined, //城市编码（如110000=北京市）
      zipcode: undefined, //邮政编码
      mername: undefined, //组合名称（如北京市朝阳区）
      lng: undefined, //经度
      lat: undefined, //纬度
      pinyin: undefined, //区域拼音（如Beijing）
      createTime: undefined, //创建时间
      updateTime: undefined, //修改时间
      creator: undefined, //创建人（关联sys_user.id，数值型匹配）
      updater: undefined, //修改人（关联sys_user.id，数值型匹配）
  };

  let form = reactive({ ...formDefault });

  const rules = {
      name: [{ required: true, message: '区域名称（如北京市、朝阳区） 必填' }],
      pid: [{ required: true, message: '上级区域ID（0=顶级区域） 必填' }],
      simplename: [{ required: true, message: '区域简称（如北京、朝阳） 必填' }],
      level: [{ required: true, message: '区域等级（1=省/直辖市，2=市，3=区/县） 必填' }],
      citycode: [{ required: true, message: '城市编码（如110000=北京市） 必填' }],
      zipcode: [{ required: true, message: '邮政编码 必填' }],
      mername: [{ required: true, message: '组合名称（如北京市朝阳区） 必填' }],
      lng: [{ required: true, message: '经度 必填' }],
      lat: [{ required: true, message: '纬度 必填' }],
      pinyin: [{ required: true, message: '区域拼音（如Beijing） 必填' }],
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
        await sysAreaApi.update(form);
      } else {
        await sysAreaApi.add(form);
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
