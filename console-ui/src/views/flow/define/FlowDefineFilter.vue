<script lang="ts" setup>
import { reactive } from 'vue';
const emit = defineEmits(['search']);

const formValue = reactive({
  flowName: '',
  flowKey: '',
  flowType: '',
});

const onSubmit = () => {
  emit('search', formValue);
};

const onReset = () => {
  formValue.flowName = '';
  formValue.flowKey = '';
  formValue.flowType = '';
};
</script>

<template>
  <el-form :inline="true" :model="formValue">
    <el-form-item label="流程名称">
      <el-input v-model="formValue.flowName" placeholder="请输入流程名称" clearable/>
    </el-form-item>
    <el-form-item label="流程编码">
      <el-input v-model="formValue.flowKey" placeholder="请输入流程编码" clearable/>
    </el-form-item>
    <el-form-item label="流程类型" style="width: 200px">
      <el-select v-model="formValue.flowType" placeholder="请选择流程类型" clearable>
        <el-option key="all" label="全部" value="" />
        <el-option key="single" label="单流程" value="single" />
        <el-option key="multi" label="多流程" value="multi" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">查询</el-button>
    </el-form-item>
    <el-form-item>
      <el-button @click="onReset">重置</el-button>
    </el-form-item>
  </el-form>
</template>
