<script lang="ts" setup>
import { PropType, ref, watch } from 'vue';
import { ElementType, RawData } from '../../types';
import { cloneDeep } from 'lodash-es';
import { ElMessage } from 'element-plus';

import { useFlowDataInject } from "@/views/flow/design/hooks/flow-data.ts";

const flowContext = useFlowDataInject();

type FeignData = RawData & {
  serviceName: string;
  path: string;
  parameters: { key: string, value: string }[]; // 修改为包含 key 和 value 的对象数组
  method: string;
  contextType: string;
  body: string;
};

function getDefaultData() {
  return {
    key: '',
    name: '',
    desc: '',
    outgoings: [],
    incomings: [],
    elementType: ElementType.FEIGN,
    log: '',
    parameters: [{ key: '', value: '' }], // 初始化为一个对象数组
    serviceName: '',
    path: '',
    contextType: '',
    method: '',
    body: ''
  };
}

const emit = defineEmits(['update', 'cancel']);
const props = defineProps({
  data: {
    type: Object as PropType<FeignData>,
    required: true,
  },
});

const nodeData = ref(getDefaultData() as FeignData);
watch(
    () => props.data,
    (val) => {
      if (val !== nodeData.value) {
        nodeData.value = Object.assign(getDefaultData(), cloneDeep(val));
      }
    },
    { immediate: true }
);

// 表单验证规则
const rules = {
  name: [{ required: true, message: '节点名称不能为空', trigger: 'blur' }],
  serviceName: [{ required: true, message: '服务名不能为空', trigger: 'blur' }],
  path: [{ required: true, message: '路径不能为空', trigger: 'blur' }],
  contextType: [{ required: true, message: '报文格式不能为空', trigger: 'blur' }],
  method: [{ required: true, message: '请求方式不能为空', trigger: 'blur' }],
  parameters: [
    { validator: (rule: any, value: any, callback: Function) => {
        for (const param of value) {
          if (!param.key || !param.value) {
            callback(new Error('参数的key和value不能为空'));
            return;
          }
        }
        callback();
      }, trigger: 'blur' }
  ]
};

// 获取表单引用
const formRef = ref(null);

// 表单验证函数
function validate() {
  return new Promise((resolve, reject) => {
    formRef.value?.validate((valid: boolean) => {
      if (valid) {
        resolve(true);
      } else {
        ElMessage.error('请填写所有必填项');
        reject(false);
      }
    });
  });
}

// 提交函数
function onSubmit() {
  validate().then(() => {
    emit('update', cloneDeep(nodeData.value));
  }).catch(() => {
    // 触发错误提示或其他逻辑（已经通过 ElMessage 提示了）
  });
}

function onCancel() {
  emit('cancel');
}

// 添加参数
function addParameter() {
  nodeData.value.parameters.push({ key: '', value: '' });
}

// 删除参数
function removeParameter(index: number) {
  nodeData.value.parameters.splice(index, 1);
}
</script>

<template>
  <div class="node-method-form">
    <el-form ref="formRef" label-position="top" :model="nodeData" :rules="rules">
      <el-form-item label="节点编码">
        <span>{{ nodeData.key }}</span>
      </el-form-item>

      <el-form-item label="节点名称" prop="name">
        <el-input v-model="nodeData.name" placeholder="请输入"></el-input>
      </el-form-item>

      <el-form-item label="节点描述" prop="desc">
        <el-input v-model="nodeData.desc" placeholder="请输入" :rows="2" type="textarea"></el-input>
      </el-form-item>

      <el-form-item label="服务名" prop="serviceName">
        <el-input v-model="nodeData.serviceName" placeholder="请输入服务名"></el-input>
      </el-form-item>

      <el-form-item label="路径" prop="path">
        <el-input v-model="nodeData.path" placeholder="请输入路径"></el-input>
      </el-form-item>

      <el-form-item label="入参" prop="parameters">
        <div v-for="(param, index) in nodeData.parameters" :key="index" class="parameter-item">
          <el-row :gutter="10" align="middle">
            <!-- Key 输入框 -->
            <el-col :span="10">
              <el-input v-model="param.key" placeholder="请输入参数的key" />
            </el-col>

            <!-- Value 输入框 -->
            <el-col :span="8">
              <el-input v-model="param.value" placeholder="请输入参数的value" style="margin-right: 4px;" />
            </el-col>

            <!-- 操作按钮 -->
            <el-col :span="4" class="button-group" style="display: flex; align-items: center;">
              <!-- 加号按钮 -->
              <el-button
                  size="mini"
                  @click="addParameter"
                  type="primary"
                  class="add-btn"
                  style="background-color: cornflowerblue; border-color: cornflowerblue; color: #fff; padding: 4px 8px; font-size: 14px; margin-left: 2px;"
              >
                +
              </el-button>

              <!-- 减号按钮 -->
              <el-button
                  size="mini"
                  type="danger"
                  @click="removeParameter(index)"
                  v-if="nodeData.parameters.length > 1"
                  class="remove-btn"
                  style="background-color: red; border-color: red; color: #fff; padding: 4px 8px; font-size: 14px; margin-left: 6px;"
              >
                -
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-form-item>

      <el-form-item label="body" prop="body">
        <el-input v-model="nodeData.body" placeholder="请输入" :rows="2" type="textarea"></el-input>
      </el-form-item>

      <el-form-item label="报文格式" prop="contextType">
        <el-select v-model="nodeData.contextType" placeholder="请选择报文格式">
          <el-option key="json" label="json" value="json" />
          <el-option key="xml" label="xml" value="xml" />
          <el-option key="text" label="text" value="text" />
        </el-select>
      </el-form-item>

      <el-form-item label="请求方式" prop="method">
        <el-select v-model="nodeData.method" placeholder="请选择请求方式">
          <el-option key="get" label="get" value="get" />
          <el-option key="post" label="post" value="post" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">确定</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
