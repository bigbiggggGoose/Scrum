<!--
  * Scrum Activities 主页面
  * 用于测试和展示Scrum Activities功能
-->
<template>
  <div class="scrum-activities-container">
    <a-card title="Scrum Activities 测试页面" :bordered="false">
      <a-alert 
        message="Scrum Activities 功能已整合到Smart Admin中" 
        description="这是一个测试页面，用于验证Scrum Activities功能是否正常工作。"
        type="success" 
        show-icon 
        style="margin-bottom: 16px"
      />
      
      <a-row :gutter="16">
        <a-col :span="12">
          <a-card title="功能说明" size="small">
            <ul>
              <li>✅ 后端API已整合</li>
              <li>✅ 前端页面已创建</li>
              <li>✅ 数据库表结构已定义</li>
              <li>⏳ 菜单配置需要手动添加</li>
              <li>⏳ 权限配置需要手动添加</li>
            </ul>
          </a-card>
        </a-col>
        <a-col :span="12">
          <a-card title="下一步操作" size="small">
            <ol>
              <li>执行数据库SQL脚本创建表</li>
              <li>在后台管理系统中添加菜单</li>
              <li>配置相应的权限</li>
              <li>测试CRUD功能</li>
            </ol>
          </a-card>
        </a-col>
      </a-row>
      
      <a-divider />
      
      <a-space>
        <a-button type="primary" @click="goToList">
          <template #icon>
            <TableOutlined />
          </template>
          查看列表页面
        </a-button>
        <a-button @click="testApi">
          <template #icon>
            <ApiOutlined />
          </template>
          测试API连接
        </a-button>
      </a-space>
    </a-card>
  </div>
</template>

<script setup>
import { message } from 'ant-design-vue';
import { TableOutlined, ApiOutlined } from '@ant-design/icons-vue';
import { sysActivitiesApi } from '/@/api/business/scrum/sys-activities/sys-activities-api';

// 跳转到列表页面
function goToList() {
  // 这里应该跳转到实际的列表页面
  message.info('列表页面功能已准备就绪，需要配置菜单后才能访问');
}

// 测试API连接
async function testApi() {
  try {
    message.loading('正在测试API连接...', 0);
    const result = await sysActivitiesApi.queryPage({ pageNum: 1, pageSize: 10 });
    message.destroy();
    message.success('API连接正常！');
    console.log('API测试结果:', result);
  } catch (error) {
    message.destroy();
    message.error('API连接失败：' + error.message);
    console.error('API测试错误:', error);
  }
}
</script>

<style scoped>
.scrum-activities-container {
  padding: 16px;
}
</style>
