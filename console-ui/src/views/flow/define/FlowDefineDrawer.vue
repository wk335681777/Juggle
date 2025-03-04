<script setup lang="ts">
import { computed, nextTick, reactive, ref } from 'vue';
import { FormInstance, FormRules } from 'element-plus';
import { FlowDefineInfo } from '@/typings';
import { flowDefineService } from '@/service';
import ResizableDrawer from "@/components/common/ResizableDrawer.vue";

const flowDefineDrawerVisible = ref(false);
const formRef = ref<FormInstance>();
const editItem = ref<Record<string, any>>();
const flowDefineFormValue = reactive<FlowDefineInfo>(getDefaultFlowDefine());

function getDefaultFlowDefine() {
  return {
    id: null,
    flowKey: '',
    flowName: '',
    flowType: '',
    remark: '',
    flowInputParams: [],
    flowOutputParams: [],
  };
}

const rules = reactive<FormRules>({
  flowKey: [
    {
      required: false,  // flowKey 为非必填
      message: '请输入流程编码',
      trigger: 'blur',  // 触发校验时机
    },
    {
      validator: (rule, value, callback) => {
        if (value && !/^[a-zA-Z0-9_]+$/.test(value)) {
          // 如果 flowKey 有值且不符合正则，回调并显示错误
          callback(new Error('流程编码必须包含字母、数字或下划线'));
        } else {
          // 如果校验通过，回调为空
          callback();
        }
      },
      trigger: 'blur',  // 触发校验时机
    }
  ],
  flowName: [{ required: true, message: '请输入流程名称', trigger: 'blur' }],
  flowType: [{ required: true, message: '请选择流程类型', trigger: 'blur' }],
});


const emit = defineEmits(['add', 'edit']);

function onCancel() {
  flowDefineDrawerVisible.value = false;
}

async function onSubmit() {
  if (!formRef.value) return;

  console.log('formRef:', formRef.value);
  const valid = await formRef.value.validate();

  if (!valid) {
    console.log('校验失败');
    return;
  }

  flowDefineDrawerVisible.value = false;
  if (editItem.value) {
    emit('edit', { ...editItem.value, ...flowDefineFormValue });
  } else {
    emit('add', flowDefineFormValue);
  }
}

async function open(item?: Record<string, any>) {
  editItem.value = item;
  flowDefineDrawerVisible.value = true;

  nextTick(async () => {
    formRef.value?.resetFields();
    if (item) {
      // 编辑模式
      const res = await flowDefineService.getDefineInfo(item.id);
      if (res.success) {
        Object.assign(flowDefineFormValue, res.result);
      }
    } else {
      // 新增模式
      Object.assign(flowDefineFormValue, getDefaultFlowDefine());

      // 向后端请求自动生成 flowKey**
      const res = await flowDefineService.generateFlowKeyInfo();
      if (res.success && res.result) {
        flowDefineFormValue.flowKey = res.result; // 确保 flowKey 被赋值
      }
    }
  });
}


const title = computed(() => {
  if (editItem.value) {
    return '编辑流程定义';
  }
  return '新增流程定义';
});

defineExpose({ open });
</script>

<template>
  <ResizableDrawer v-model="flowDefineDrawerVisible" :size="600" :title="title" drawer-key="FLOW_DEFINE" destroyOnClose>
    <div>
      <el-form ref="formRef" label-position="top" :model="flowDefineFormValue" :rules="rules">
        <!-- 新建时显示流程编码，编辑时不显示 -->
        <el-form-item v-if="!editItem" label="流程编码" prop="flowKey">
          <el-input v-model="flowDefineFormValue.flowKey" maxlength="30" />
        </el-form-item>

        <el-form-item label="流程名称" prop="flowName">
          <el-input v-model="flowDefineFormValue.flowName" maxlength="30" />
        </el-form-item>
        <el-form-item label="流程类型" prop="flowType">
          <el-select placeholder="请选择流程类型" v-model="flowDefineFormValue.flowType">
            <el-option label="单流程" value="sync" />
            <el-option label="多流程" value="async" />
          </el-select>
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
