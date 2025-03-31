<script lang="ts" setup>
import { reactive, ref } from 'vue';
const emit = defineEmits(['search']);

defineProps({
  treeData: {
    type: Array,
    default: [],
  },
});

const defaultProps = {
  value: "path",
  label: "name",
};

const formValue = reactive({
  flowName: '',
  flowKey: '',
  flowType: '',
  tagPath: '',
});

const onSubmit = () => {
  emit('search', formValue);
};

const onReset = () => {
  formValue.flowName = '';
  formValue.flowKey = '';
  formValue.flowType = '';
  formValue.tagPath = '';
};

function changeTag(path) {
  formValue.tagPath = path;
}

defineExpose({ changeTag });
</script>

<template>
  <el-form :inline="true" :model="formValue">
    <el-form-item label="流程名称">
      <el-input v-model="formValue.flowName" placeholder="请输入流程名称" style="width: 150px" clearable/>
    </el-form-item>
    <el-form-item label="流程编码">
      <el-input v-model="formValue.flowKey" placeholder="请输入流程编码" style="width: 150px" clearable/>
    </el-form-item>
    <el-form-item label="流程类型" style="width: 160px">
      <el-select v-model="formValue.flowType" placeholder="请选择流程类型" clearable>
        <el-option key="all" label="全部" value="" />
        <el-option key="single" label="单流程" value="single" />
        <el-option key="multi" label="多流程" value="multi" />
      </el-select>
    </el-form-item>
    <el-form-item label="流程标签" prop="tags" style="width: 200px">
      <el-tree-select
          v-model="formValue.tagPath"
          :data="treeData"
          node-key="id"
          clearable
          placeholder="请选择"
          check-strictly
          default-expand-all
          :props="defaultProps"
          @change="handleSelectChange"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">查询</el-button>
    </el-form-item>
<!--    <el-form-item>-->
<!--      <el-button @click="onReset">重置</el-button>-->
<!--    </el-form-item>-->
  </el-form>
</template>
