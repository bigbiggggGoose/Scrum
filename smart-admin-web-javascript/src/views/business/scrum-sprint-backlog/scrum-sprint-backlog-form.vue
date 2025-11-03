<!--
  * Sprint待办事项 表单
  *
  * @Author:    system
  * @Date:      2025-01-01 00:00:00
  * @Copyright  system
-->
<template>
  <a-drawer
      :title="form.id ? '编辑待办' : '新建待办'"
      :width="800"
      :open="visibleFlag"
      @close="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" >
      <a-form-item label="所属Sprint" name="sprintId">
        <a-input-number v-model:value="form.sprintId" style="width:100%" :disabled="true" />
      </a-form-item>
      <a-form-item label="任务标题" name="backlogTitle">
        <a-input v-model:value="form.backlogTitle" placeholder="请输入任务标题" />
      </a-form-item>
      <a-form-item label="执行状态" name="backlogStatus">
        <a-select v-model:value="form.backlogStatus" :options="SPRINT_BACKLOG_STATUS_OPTIONS" allowClear />
      </a-form-item>
      <a-form-item label="优先级" name="priority">
        <a-select v-model:value="form.priority" :options="SPRINT_BACKLOG_PRIORITY_OPTIONS" allowClear />
      </a-form-item>
      <a-form-item label="预计工时(小时)" name="estimatedHours">
        <a-input-number v-model:value="form.estimatedHours" :min="0" :step="0.5" style="width:100%" />
      </a-form-item>
      <a-form-item label="实际工时(小时)" name="actualHours">
        <a-input-number v-model:value="form.actualHours" :min="0" :step="0.5" style="width:100%" />
      </a-form-item>
      <a-form-item label="任务目标" name="backlogGoal">
        <a-input v-model:value="form.backlogGoal" placeholder="请输入目标" />
      </a-form-item>
      <a-form-item label="任务描述" name="backlogDescription">
        <a-textarea v-model:value="form.backlogDescription" rows="4" placeholder="请输入任务描述" />
      </a-form-item>

      <div class="smart-form-btn">
        <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        <a-button class="smart-margin-left10" @click="onClose">取消</a-button>
      </div>
    </a-form>
  </a-drawer>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { scrumSprintBacklogApi } from '/@/api/business/scrum-sprint-backlog/scrum-sprint-backlog-api';
import { SPRINT_BACKLOG_STATUS_OPTIONS, SPRINT_BACKLOG_PRIORITY_OPTIONS } from '/@/api/business/scrum-sprint-backlog/scrum-sprint-backlog-const';
import { smartSentry } from '/@/lib/smart-sentry';

const emit = defineEmits(['reloadList']);

const visibleFlag = ref(false);
const formRef = ref();
const submitLoading = ref(false);

const form = reactive({
  id: null,
  sprintId: null,
  backlogTitle: null,
  backlogStatus: '1',
  priority: 2,
  estimatedHours: null,
  actualHours: null,
  backlogGoal: null,
  backlogDescription: null,
});

const rules = {
  backlogTitle: [{ required: true, message: '请输入任务标题' }],
  sprintId: [{ required: true, message: '缺少SprintId' }],
};

function show(sprintId, data){
  visibleFlag.value = true;
  if(data){
    Object.assign(form, data);
  } else {
    Object.assign(form, { id:null, backlogTitle:null, backlogStatus:'1', priority:2, estimatedHours:null, actualHours:null, backlogGoal:null, backlogDescription:null });
  }
  form.sprintId = sprintId;
}

function onClose(){
  visibleFlag.value = false;
}

async function onSubmit(){
  try{
    await formRef.value.validate();
    submitLoading.value = true;
    if(form.id){
      await scrumSprintBacklogApi.update(form);
      message.success('更新成功');
    }else{
      await scrumSprintBacklogApi.add(form);
      message.success('新增成功');
    }
    emit('reloadList');
    onClose();
  }catch(e){
    smartSentry.captureError(e);
  }finally{
    submitLoading.value = false;
  }
}

defineExpose({ show });
</script>

<style scoped>
.smart-form-btn{ margin-top:16px; }
</style>