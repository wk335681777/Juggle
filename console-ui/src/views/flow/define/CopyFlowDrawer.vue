<script setup lang="ts">
import { computed, nextTick, reactive, ref } from 'vue';
import { FormInstance, FormRules } from 'element-plus';
import { FlowDefineInfo } from '@/typings';
import { flowDefineService } from '@/service';
import ResizableDrawer from "@/components/common/ResizableDrawer.vue";

const flowDefineDrawerVisible = ref(false);
const formRef = ref<FormInstance>();
const copyItem = ref<Record<string, any>>();
const flowDefineFormValue = reactive<FlowDefineInfo>(getDefaultFlowDefine());

function getDefaultFlowDefine() {
  return {
    id: '',
    flowName: '',
    remark: '',
  };
}

const rules = reactive<FormRules>({
  flowName: [{ required: true, message: '请输入流程名称', trigger: 'blur' }],
  flowType: [{ required: true, message: '请选择流程类型', trigger: 'blur' }],
});

const emit = defineEmits(['copy']);

function onCancel() {
  flowDefineDrawerVisible.value = false;
}

async function onSubmit() {
  if (!formRef.value) return;
  const valid = await formRef.value?.validate(() => {});
  if (!valid) {
    return;
  }

  flowDefineDrawerVisible.value = false;
  if (copyItem.value) {
    emit('copy', { ...copyItem.value, ...flowDefineFormValue });
  }
}

function open(item?: Record<string, any>) {
  copyItem.value = item;
  flowDefineDrawerVisible.value = true;
  nextTick(async () => {
    formRef.value?.resetFields();
    if (item) {
      const res = await flowDefineService.getDefineInfo(item.id);
      if (res.success) {
        flowDefineFormValue.id = res.result.id;
        flowDefineFormValue.flowName = res.result.flowName;
        flowDefineFormValue.remark = res.result.remark;
      }
    } else {
      Object.assign(flowDefineFormValue, getDefaultFlowDefine());
    }
  });
}

const title = computed(() => {
  if (copyItem.value) {
    return '复制流程定义';
  }
});

defineExpose({ open });
</script>

<template>
  <ResizableDrawer v-model="flowDefineDrawerVisible" :size="600" :title="title" drawer-key="FLOW_DEFINE" destroyOnClose>
    <div>
      <el-form ref="formRef" label-position="top" :model="flowDefineFormValue" :rules="rules">
        <el-form-item label="流程名称" prop="flowName">
          <el-input v-model="flowDefineFormValue.flowName" maxlength="30" />
        </el-form-item>
        <el-form-item label="流程描述">
          <el-input type="textarea" v-model="flowDefineFormValue.remark" maxlength="120" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">确定</el-button>
          <el-button @click="onCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </ResizableDrawer>
</template>

<style scoped></style>
