<script lang="ts" setup>
import { reactive } from 'vue';
const emit = defineEmits(['search']);

const formValue = reactive({
  name: '',
  url: '',
});

const onSubmit = () => {
  emit('search', formValue);
};

const onReset = () => {
  formValue.name = '';
  formValue.url = '';
};
</script>

<template>
  <el-form :inline="true" :model="formValue">
    <el-form-item label="名称">
      <el-input v-model="formValue.name" placeholder="请输入名称" />
    </el-form-item>
    <el-form-item label="url">
      <el-input v-model="formValue.url" placeholder="请输入url" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">查询</el-button>
    </el-form-item>
    <el-form-item>
      <el-button @click="onReset">重置</el-button>
    </el-form-item>
  </el-form>
</template>
