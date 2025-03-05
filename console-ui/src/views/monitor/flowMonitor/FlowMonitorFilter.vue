<script lang="ts" setup>
import { reactive } from 'vue';
const emit = defineEmits(['search']);

const formValue = reactive({
  flowName: '',
  flowKey: '',
});

const onSubmit = () => {
  emit('search', formValue);
};

const onReset = () => {
  formValue.flowName = '';
  formValue.flowKey = '';
};
</script>

<template>
  <el-form :inline="true" :model="formValue">
    <el-form-item label="流程名称">
      <el-input v-model="formValue.flowName" placeholder="请输入名称" />
    </el-form-item>
    <el-form-item label="流程编码">
      <el-input v-model="formValue.flowKey" placeholder="请输编码" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">查询</el-button>
    </el-form-item>
    <el-form-item>
      <el-button @click="onReset">重置</el-button>
    </el-form-item>
  </el-form>
</template>
