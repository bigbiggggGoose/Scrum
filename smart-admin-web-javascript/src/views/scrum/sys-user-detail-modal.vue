<!--
  * 用户信息表 详情弹窗（只读）
  *
  * @Author:    oyt
  * @Date:      2025-10-14
  * @Copyright  oyt
-->
<template>
  <a-modal :open="visible" :title="'详情'" :width="800" :footer="null" @cancel="onClose">
    <a-descriptions bordered :column="2" size="small">
      <a-descriptions-item label="登录名">{{ detail.username || '-' }}</a-descriptions-item>
      <a-descriptions-item label="密码">{{ detail.password || '-' }}</a-descriptions-item>
      <a-descriptions-item label="真实姓名">{{ detail.realName || '-' }}</a-descriptions-item>
      <a-descriptions-item label="工资表顺序">{{ detail.sidx ?? '-' }}</a-descriptions-item>
      <a-descriptions-item label="生日">{{ detail.birth || '-' }}</a-descriptions-item>
      <a-descriptions-item label="性别">{{ formatSex(detail.sex) }}</a-descriptions-item>
      <a-descriptions-item label="邮箱">{{ detail.email || '-' }}</a-descriptions-item>
      <a-descriptions-item label="手机号">{{ detail.phone || '-' }}</a-descriptions-item>
      <a-descriptions-item label="角色ID">{{ detail.roleId ?? '-' }}</a-descriptions-item>
      <a-descriptions-item label="部门ID">{{ detail.deptId ?? '-' }}</a-descriptions-item>
      <a-descriptions-item label="是否禁用">{{ formatDisable(detail.isDisable) }}</a-descriptions-item>
      <a-descriptions-item label="状态">{{ formatStatus(detail.status) }}</a-descriptions-item>
    </a-descriptions>
  </a-modal>
</template>

<script setup>
  import { ref } from 'vue';

  defineExpose({
    show,
  });

  const visible = ref(false);
  const detail = ref({});

  function show(record) {
    detail.value = { ...(record || {}) };
    visible.value = true;
  }

  function onClose() {
    visible.value = false;
  }

  function formatSex(val) {
    if (val === 1) return '男';
    if (val === 2) return '女';
    return '-';
  }

  function formatDisable(val) {
    if (val === 1) return '是';
    if (val === 2) return '否';
    return '-';
  }

  function formatStatus(val) {
    if (val === 1) return '在职';
    if (val === 0) return '离职';
    return '-';
  }
</script>

<style scoped>
</style>